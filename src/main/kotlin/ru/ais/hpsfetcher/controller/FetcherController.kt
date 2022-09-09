package ru.ais.hpsfetcher.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.ais.hpsfetcher.dto.FetchDataRequest
import ru.ais.hpsfetcher.dto.FetchDataResponse
import ru.ais.hpsfetcher.model.DataSource
import ru.ais.hpsfetcher.service.DataSourceService
import ru.ais.hpsfetcher.service.FetcherService

@RestController
class FetcherController(
    private val fetcherService: FetcherService,
    private val dataSourceService: DataSourceService,

    ) {
    @PostMapping("/fetch-data")
    fun fetchData(@RequestBody request: FetchDataRequest): FetchDataResponse =
        fetcherService.requestStocksForCategory(
            dataSourceEnum = request.dataSource,
            categoryId = request.categoryId!!,
            regionId = request.regionId,
            shopId = request.shopId
        )
            .let { products ->
                FetchDataResponse(
                    categoryId = request.categoryId,
                    dataSource = request.dataSource,
                    regionId = request.regionId,
                    shopId = request.shopId,
                    productsCount = products.size
                )
            }

    @PostMapping("/create")
    fun create(@RequestBody request: FetchDataRequest): DataSource =
        dataSourceService.createDataSource(
            dataSourceEnum = request.dataSource,
            regionId = request.regionId,
            shopId = request.shopId
        )

}

