package ru.ais.hpsfetcher.dto

import ru.ais.hpsfetcher.model.Product

data class PagedResponse(
    val count: Int,
    val response: String?,
    val productList: List<Product>,
)
