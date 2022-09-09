package ru.ais.hpsfetcher.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import ru.ais.hpsfetcher.model.DataSource
import ru.ais.hpsfetcher.model.FetchRequest
import ru.ais.hpsfetcher.model.FetchSession
import ru.ais.hpsfetcher.model.Product
import java.time.ZonedDateTime

interface DataSourceRepository : CrudRepository<DataSource, Int> {

    fun findBydataSourceTypeIdAndShopIdAndRegionId(dataSourceTypeId: Int, shopId: String,
                                           regionId: String): DataSource?
}

interface FetchRequestRepository : CrudRepository<FetchRequest, Int> {

    @Transactional
    fun deleteByRequest(request: String)

    @Query("select * from hypershop.FetchRequest " +
            "where Request = :request and HttpStatus = 200 " +
            "and CreateDateTime >= :createDateTime",
        nativeQuery = true)
    fun findCachedRequest(@Param("request") request: String,
                          @Param("createDateTime") createDateTime: ZonedDateTime): List<FetchRequest>
}

interface FetchSessionRepository : CrudRepository<FetchSession, Int>

interface ProductRepository : JpaRepository<Product, Int>
