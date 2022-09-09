package ru.ais.hpsfetcher.service.adapter

import ru.ais.hpsfetcher.model.FetchSession
import ru.ais.hpsfetcher.model.Product

interface FetchAdapter {

    fun requestStocksForCategory(fetchSession: FetchSession, categoryId: String,
                                 regionId: String?, shopId: String?): List<Product>
}
