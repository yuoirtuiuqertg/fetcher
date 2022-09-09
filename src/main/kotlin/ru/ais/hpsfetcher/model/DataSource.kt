package ru.ais.hpsfetcher.model

import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "DataSource", schema = "hypershop", catalog = "MSSQLApt2")
class DataSource {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    var id: Int? = null

    @Column(name = "Name", nullable = false, length = 255)
    var name: String? = null

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "DataSourceTypeId", updatable = false )
//    var dataSourceType: DataSourceType? = null

    @Column(name = "DataSourceTypeId", nullable = false)
    var dataSourceTypeId: Int? = null

    @Column(name = "ShopId", nullable = true, length = 255)
    var shopId: String? = null

    @Column(name = "RegionId", nullable = true, length = 255)
    var regionId: String? = null

    @Column(name = "CreateDateTime", nullable = false)
    var createDateTime: ZonedDateTime? = null

    @Column(name = "Reserv1", nullable = true, length = 255)
    var reserv1: String? = null

    @Column(name = "Reserv2", nullable = true, length = 255)
    var reserv2: String? = null

    @Column(name = "Reserv3", nullable = true, length = 255)
    var reserv3: String? = null

    @Column(name = "Reserv4", nullable = true, length = 255)
    var reserv4: String? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DataSource
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (name != null) name != that.name else that.name != null) return false
        if (if (dataSourceTypeId != null) dataSourceTypeId != that.dataSourceTypeId else that.dataSourceTypeId != null) return false
        if (if (shopId != null) shopId != that.shopId else that.shopId != null) return false
        if (if (regionId != null) regionId != that.regionId else that.regionId != null) return false
        if (if (createDateTime != null) createDateTime != that.createDateTime else that.createDateTime != null) return false
        if (if (reserv1 != null) reserv1 != that.reserv1 else that.reserv1 != null) return false
        if (if (reserv2 != null) reserv2 != that.reserv2 else that.reserv2 != null) return false
        if (if (reserv3 != null) reserv3 != that.reserv3 else that.reserv3 != null) return false
        return if (if (reserv4 != null) reserv4 != that.reserv4 else that.reserv4 != null) false else true
    }

    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 0
        result = 31 * result + if (name != null) name.hashCode() else 0
        result = 31 * result + if (dataSourceTypeId != null) dataSourceTypeId.hashCode() else 0
        result = 31 * result + if (shopId != null) shopId.hashCode() else 0
        result = 31 * result + if (regionId != null) regionId.hashCode() else 0
        result = 31 * result + if (createDateTime != null) createDateTime.hashCode() else 0
        result = 31 * result + if (reserv1 != null) reserv1.hashCode() else 0
        result = 31 * result + if (reserv2 != null) reserv2.hashCode() else 0
        result = 31 * result + if (reserv3 != null) reserv3.hashCode() else 0
        result = 31 * result + if (reserv4 != null) reserv4.hashCode() else 0
        return result
    }
}