package ru.ais.hpsfetcher.service.adapter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import ru.ais.hpsfetcher.dto.PagedResponse
import ru.ais.hpsfetcher.model.FetchRequest
import ru.ais.hpsfetcher.model.FetchSession
import ru.ais.hpsfetcher.model.Product
import ru.ais.hpsfetcher.service.FetchRequestService
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class DixyAdapter(
    private val fetchRequestService: FetchRequestService,
    private val restTemplate: RestTemplate,
): FetchAdapter {
    companion object {
        const val REQUEST_DELAY: Long = 1 * 3000
        internal val logger = LoggerFactory.getLogger(DixyAdapter::class.java)
        private val objectMapper = jacksonObjectMapper()
        private val fetchUrlTemplate = "https://dostavka.dixy.ru/catalog/#category#/"
    }

    override fun requestStocksForCategory(
        fetchSession: FetchSession, categoryId: String, regionId: String?,
        shopId: String?
    ): List<Product> {
        val headers = org.springframework.http.HttpHeaders()
        val headersMap: MultiValueMap<String, String> = LinkedMultiValueMap()

        headersMap.add("cookie", "store_id=$shopId")
        headers.addAll(headersMap)

        val url = fetchUrlTemplate
            .replace("#category#", categoryId.toString())


        if(shopId != null) {
            return makeAllPagesRequest(url = url, headers = headers, shopId = shopId)
        } else {
            val prodList = mutableListOf<Product>()
            return prodList
        }
    }

    fun makePagedRequest(
        url: String, httpMethod: HttpMethod = HttpMethod.GET,
        headers: org.springframework.http.HttpHeaders, body: Any = ""
    ): PagedResponse? =
        convertHTML(makeRequestWithCache(url, httpMethod, headers, body))

    fun makeAllPagesRequest(
        url: String, shopId: String,httpMethod: HttpMethod = HttpMethod.GET,
        headers: org.springframework.http.HttpHeaders, body: Any = "", limit: Int = 100
    ): List<Product> {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("cookie", "store_id=$shopId")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        val str : String = response.body();
        var document = Jsoup.parse(str);
        val elements  =document.getElementsByAttributeValue("class", "dark_link")
        var pag = elements.text()
        var pages = 1
        if (pag != ""){
            pages = pag.substringAfterLast(" ").toInt()
        }
        val prodList = mutableListOf<Product>()
        var page: Int = 1
        var pp = pages
        var finUrl = url
        while(page <= pages) {
            finUrl = "$url?PAGEN_1=$page"
            val resp = makePagedRequest(finUrl, httpMethod, headers, body)
            if (resp != null) {
                prodList.addAll(resp.productList)
            }

            page++

            Thread.sleep(500)
        }
        return prodList
    }

    internal fun makeRequestWithCache(
        url: String, httpMethod: HttpMethod = HttpMethod.GET, headers: org.springframework.http.HttpHeaders,
        body: Any = ""
    ): FetchRequest =
        fetchRequestService.loadResponseFromCache(url)
            ?: makeRequestInternal(url, httpMethod, headers, body, String::class.java)
                .let {
                    fetchRequestService.saveResponseToCache(
                        url = url,
                        response = it.body,
                        httpStatus = it.statusCode
                    )
                }
                .also { Thread.sleep(REQUEST_DELAY) }

    internal fun <T> makeRequestInternal(
        url: String, httpMethod: HttpMethod = HttpMethod.GET, headers: org.springframework.http.HttpHeaders,
        body: Any = "", responseType: Class<T>
    ): ResponseEntity<T> =
        try {
            val httpEntity = HttpEntity<Any>(body, headers)
            restTemplate.exchange(url, httpMethod, httpEntity, responseType)
        } catch (ex: Exception) {
            val bodyStr = objectMapper.writeValueAsString(body)
            logger.error(
                "Error in request from url = (${httpMethod.name})${url}. " +
                        "responseType: ${responseType.simpleName}. body: $bodyStr.", ex
            )
            throw ex
        }


    private fun convertHTML(fetchRequest: FetchRequest) : PagedResponse? {

        val htmlPage = fetchRequest.response
        val pL = fetchRequest.id?.let { htmlPage.toProductList(it) }
        val count = pL?.size

        val pagedResponse = count?.let {
            PagedResponse(
                count = count,
                response = htmlPage,
                productList =pL,
            )
        }
        return pagedResponse
    }


    private fun String?.toProductList(fetchRequestId: Int): List<Product> {

        this ?: return emptyList()
        val prodList = mutableListOf<Product>()
        var document = Jsoup.parse(this)
        val elements  =document.getElementsByAttributeValue("class", "na_item-card")
        val title = document.title()
        for (a in elements) {
            Product().let { product: Product ->
                product.stockId =a.getElementsByAttribute("data-id").attr("data-id")
                product.fetchRequestId = fetchRequestId
                product.stockName =a.getElementsByAttributeValue("class", "card-name").text().toString()
                product.price1 = a.getElementsByAttributeValue("class","card-prices").text().substringAfterLast(" ").replace("₽","")
                product.webPageLink = "https://dostavka.dixy.ru"+ a.getElementsByAttributeValue("class", "card-link").attr("href")
                product.producerId = "-------"
                product.producerName ="https://dostavka.dixy.ru"+a.getElementsByAttribute("data-src").attr("data-src")
                product.stockGroup1Id = "-------"
                product.stockGroup1Name = title
                product.reserv1 = "-------"
                product.reserv2 = "-------"
                product
                prodList.add(product)
            }


        }
        return prodList
    }
}
