package ru.ais.hpsfetcher.dto

import ru.ais.hpsfetcher.model.enum.DataSourceEnum

data class FetchDataResponse(
    val categoryId: String,
    val dataSource: DataSourceEnum,
    val regionId: String?,
    val shopId: String?,
    val productsCount: Int,
)
