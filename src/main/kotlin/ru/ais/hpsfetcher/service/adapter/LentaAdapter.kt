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
class LentaAdapter(
    private val fetchRequestService: FetchRequestService,
    private val restTemplate: RestTemplate,
): FetchAdapter {
    companion object {
        const val REQUEST_DELAY: Long = 1 * 3000
        internal val logger = LoggerFactory.getLogger(LentaAdapter::class.java)
        private val objectMapper = jacksonObjectMapper()
        private const val fetchSort = "ByCardPriceAsc"
        private const val fetchUrlTemplate = "https://lenta.com/catalog/#category#/?sorting=#sort#"
    }

    override fun requestStocksForCategory(
        fetchSession: FetchSession, categoryId: String, regionId: String?,
        shopId: String?
    ): List<Product> {
        val headers = org.springframework.http.HttpHeaders()
        val headersMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        headersMap.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36" +
                       " (KHTML, like Gecko) Chrome/102.0.5005.134 YaBrowser/22.7.0.1925 Yowser/2.5 Safari/537.36")

        headersMap.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp," +
                       "image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")

        headersMap.add("Accept-Language", "ru,en;q=0.9")
        headersMap.add("Cookie", "CityCookie=$regionId; Store=$shopId")
        headers.addAll(headersMap)

        val url = fetchUrlTemplate
            .replace("#category#", categoryId)
            .replace("#sort#", fetchSort)

        return makeAllPagesRequest(url = url, headers = headers, regionId = regionId, shopId = shopId)
    }

    fun makePagedRequest(
        url: String, httpMethod: HttpMethod = HttpMethod.GET,
        headers: org.springframework.http.HttpHeaders, body: Any = ""
    ): PagedResponse? =
        convertHTML(makeRequestWithCache(url, httpMethod, headers, body))

    fun receivePageResponse(url: String, shopId: String?, regionId: String?): String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Cookie", "CityCookie=$regionId; Store=$shopId")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun getNumsPages(request : String) : Int {
        val document = Jsoup.parse(request)
        val elements  =document.getElementsByAttributeValue("class", "next")
        val pag = elements.select("a").attr("rel")
                  .replace("1 == ", "")
                  .replace(" ? 'nofollow' : 'next'", "")
        var pages = 1
        if (pag != ""){
            pages = pag.toInt()
        }
        return pages
    }

    fun makeAllPagesRequest(
        url: String, shopId: String?,httpMethod: HttpMethod = HttpMethod.GET, regionId: String?,
        headers: org.springframework.http.HttpHeaders, body: Any = "", limit: Int = 100
    ): List<Product> {
        val pages = getNumsPages(receivePageResponse(url,shopId,regionId))
        val prodList = mutableListOf<Product>()
        var page = 1
        var finUrl = url
        for(a in 1..pages) {
            val resp = makePagedRequest(finUrl, httpMethod, headers, body)
            if (resp != null) {
                prodList.addAll(resp.productList)
            }
            page++
            finUrl = "$url&page=$page"
            Thread.sleep(5000)
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


    private fun convertHTML(fetchRequest: FetchRequest): PagedResponse? {
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
        val document = Jsoup.parse(this)
        val elements  =document.getElementsByAttributeValue("class", "article__block js-catalog-container")
        val temp1 = JSONObject(elements.attr("data-catalog-data"))
        val jsonParamList = temp1.optJSONArray("skus")
        for (productJson in 0 until jsonParamList.length()) {
            prodList.add(Product().let { product: Product ->
                product.id = 1
                product.stockId = jsonParamList.getJSONObject(productJson)["code"].toString()
                product.fetchRequestId = fetchRequestId
                product.stockName = jsonParamList.getJSONObject(productJson)["title"].toString()
                product.price1 = jsonParamList.getJSONObject(productJson).optJSONObject("regularPrice")["value"].toString()
                product.webPageLink = "https://lenta.com" + jsonParamList.getJSONObject(productJson)["skuUrl"].toString()
                product.producerId = jsonParamList.getJSONObject(productJson)["code"].toString()
                product.producerName = jsonParamList.getJSONObject(productJson)["brand"].toString()
                product.stockGroup1Id = "00"
                product.stockGroup1Name = jsonParamList.getJSONObject(productJson)["gaCategory"].toString()
                product.reserv1 = "-------"
                product.reserv2 = jsonParamList.getJSONObject(productJson)["subTitle"].toString()
                product

            })



        }
        return prodList
    }
}


