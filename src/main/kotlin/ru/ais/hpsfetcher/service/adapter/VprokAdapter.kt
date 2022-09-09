package ru.ais.hpsfetcher.service.adapter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
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

@Service
class VprokAdapter(
    private val fetchRequestService: FetchRequestService,
    private val restTemplate: RestTemplate,
): FetchAdapter {
    companion object {
        const val REQUEST_DELAY: Long = 1 * 1000
        internal val logger = LoggerFactory.getLogger(VprokAdapter::class.java)
        private val objectMapper = jacksonObjectMapper()
        private val fetchLimit = 100
        private val fetchSort = "rate_desc"
        private val fetchUrlTemplate = "https://www.vprok.ru/webapi/v1/category-search/#category#?" +
                "limit=#limit#&sort=#sort#"
    }

    override fun requestStocksForCategory(fetchSession: FetchSession, categoryId: String, regionId: String?,
                                          shopId: String?): List<Product> {
        val headers = HttpHeaders()
        val headersMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        headersMap.add("X-API-Context-Region-Id", regionId.toString())
        headersMap.add("X-API-Context-Shop-Id", shopId.toString())
        headersMap.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36")
        headersMap.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9," +
                "image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
        headersMap.add("accept-language", "en-US,en;q=0.9")
        headers.addAll(headersMap)

        val url = fetchUrlTemplate
            .replace("#category#", categoryId.toString())
            .replace("#limit#", fetchLimit.toString())
            .replace("#sort#", fetchSort)

        return makeAllPagesRequest(url = url, headers = headers)
    }

    fun makePagedRequest(url: String, httpMethod: HttpMethod = HttpMethod.GET,
                         headers: HttpHeaders, body: Any = ""): PagedResponse? =
        makeRequestWithCache(url, httpMethod, headers, body).convertFromHtml()

    fun makeAllPagesRequest(url: String, httpMethod: HttpMethod = HttpMethod.GET,
                         headers: HttpHeaders, body: Any = "", limit: Int = 100): List<Product> {
        val prodList = mutableListOf<Product>()
        var page: Int = 1
        var finUrl = url
        while (true) {
            val resp = makePagedRequest(finUrl, httpMethod, headers, body)
                ?: break
            prodList.addAll(resp.productList)
            if (resp.count <= page * limit)
                break
            page++
            finUrl = "$url&page=$page"
        }
        return prodList
    }

    internal fun makeRequestWithCache(url: String, httpMethod: HttpMethod = HttpMethod.GET, headers: HttpHeaders,
                    body: Any = ""): FetchRequest =
        fetchRequestService.loadResponseFromCache(url)
            ?: makeRequestInternal(url, httpMethod, headers, body, String::class.java)
                .let { fetchRequestService.saveResponseToCache(url = url, response = it.body, httpStatus = it.statusCode ) }
                .also { Thread.sleep(REQUEST_DELAY) }

    internal fun <T>makeRequestInternal(url: String, httpMethod: HttpMethod = HttpMethod.GET, headers: HttpHeaders,
                                body: Any = "", responseType: Class<T>): ResponseEntity<T> =
        try {
            val httpEntity = HttpEntity<Any>(body, headers)
            restTemplate.exchange(url, httpMethod, httpEntity, responseType)
        } catch (ex: Exception) {
            val bodyStr = objectMapper.writeValueAsString(body)
            logger.error("Error in request from url = (${httpMethod.name})${url}. " +
                        "responseType: ${responseType.simpleName}. body: $bodyStr.", ex)
            throw ex
        }

    private fun FetchRequest.convertFromHtml(): PagedResponse? =
       this.response?.let { html ->
           try {
               val map: Map<String, Any> = objectMapper.readValue(html)
               PagedResponse(
                   count = map["count"]?.toString()?.toInt() ?: -1,
                   response = map["html"]?.toString(),
                   productList = map["html"]?.toString().toProductList(this.id!!),
               )
           } catch (ex: Exception) {
               logger.error("Error in converting html to map. $html", ex)
               null
           }
       }

    private fun String?.toProductList(fetchRequestId: Int): List<Product> {
        this ?: return emptyList()
        val js = Jsoup.parse(this)
        return js.select("li.js-catalog-product")
            .map { element ->
                val attributes = element?.select("div.xf-product")?.first()!!.attributes()
                Product().let { product ->
                    product.stockId = attributes["data-id"]
                    product.fetchRequestId = fetchRequestId
                    product.stockName = attributes["data-owox-product-name"]
                    product.price1 = attributes["data-owox-product-price"].toString()
                    product.webPageLink = attributes["data-product-card-url"]
                    product.producerId = attributes["data-owox-product-vendor-id"]
                    product.producerName = attributes["data-owox-product-vendor-name"]
                    product.stockGroup1Id = attributes["data-owox-category-id"]
                    product.stockGroup1Name = attributes["data-owox-category-name"]
                    product.reserv1 = attributes["data-owox-subcategory-id"]
                    product.reserv2 = attributes["data-owox-subcategory-name"]
                    product
                }
            }
    }
}
