package ru.ais.hpsfetcher.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ru.ais.hpsfetcher.model.enum.DataSourceEnum

@JsonIgnoreProperties(ignoreUnknown = true)
data class FetchDataRequest(
    val categoryId: String,
    val dataSource: DataSourceEnum,
    val regionId: String,
    val shopId: String
)
