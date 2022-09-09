package ru.ais.hpsfetcher.service

import org.springframework.stereotype.Service
import ru.ais.hpsfetcher.model.Product
import ru.ais.hpsfetcher.model.enum.DataSourceEnum
import ru.ais.hpsfetcher.repository.ProductRepository
import ru.ais.hpsfetcher.service.adapter.*
//import ru.ais.hpsfetcher.service.adapter.LentaAdapter

@Service
class FetcherService(
    private val productRepository: ProductRepository,
    private val dataSourceService: DataSourceService,
    private val fetchSessionService: FetchSessionService,
    private val vprokAdapter: VprokAdapter,
    private val pyaterochkaAdapter: PyaterochkaAdapter,
    private val lentaAdapter: LentaAdapter,
    private val dixyAdapter: DixyAdapter,
    private val globusAdapter: GlobusAdapter,
) {

    fun requestStocksForCategory(
        dataSourceEnum: DataSourceEnum, categoryId: String,
        regionId: String?, shopId: String?): List<Product> {
        var dataSource = dataSourceService.getDataSource(dataSourceEnum, regionId, shopId)
        if(dataSource == null && regionId != null && shopId != null) {
            dataSource = dataSourceService.createDataSource(dataSourceEnum,regionId,shopId)
        }
        if(dataSource != null) {
            val fetchSession = fetchSessionService.createSession(dataSource)
            val fetchAdapter = when (dataSourceEnum) {
                DataSourceEnum.VPROK -> vprokAdapter
                DataSourceEnum.PYATEROCHKA -> pyaterochkaAdapter
                DataSourceEnum.LENTA -> lentaAdapter
                DataSourceEnum.DIXY -> dixyAdapter
                DataSourceEnum.GLOBUS->globusAdapter
                else -> throw NotImplementedError("Adapter for $dataSourceEnum not found")
            }
            val products = fetchAdapter.requestStocksForCategory(
                fetchSession = fetchSession,
                categoryId = categoryId.toString(), regionId = regionId, shopId = shopId
            )
            return productRepository.saveAll(products).toList()
        } else {
            var temp =  mutableListOf<Product>()
            return temp
        }

    }
}
