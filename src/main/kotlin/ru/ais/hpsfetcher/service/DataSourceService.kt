package ru.ais.hpsfetcher.service

import org.springframework.stereotype.Service
import ru.ais.hpsfetcher.model.DataSource
import ru.ais.hpsfetcher.model.enum.DataSourceEnum
import ru.ais.hpsfetcher.repository.DataSourceRepository

@Service
class DataSourceService(
    private val dataSourceRepository: DataSourceRepository,
    ) {
    fun getDataSource(dataSourceEnum: DataSourceEnum,
                      regionId: String?, shopId: String?): DataSource? {
        val dataSource = if (regionId == null || shopId == null)
            dataSourceRepository.findById(dataSourceEnum.id).orElseGet(null)
        else
            dataSourceRepository.findBydataSourceTypeIdAndShopIdAndRegionId(
                dataSourceTypeId = dataSourceEnum.id,
                shopId = shopId,
                regionId = regionId)

        return dataSource
    }

    fun createDataSource(dataSourceEnum: DataSourceEnum,regionId: String,shopId: String) : DataSource {
        return DataSource().let{dataSource: DataSource ->
            dataSource.name = "${dataSourceEnum.name}_${dataSourceEnum.id}"
            dataSource.dataSourceTypeId = dataSourceEnum.id
            dataSource.shopId = shopId
            dataSource.regionId = regionId
            dataSourceRepository.save(dataSource)
        }
    }
}
