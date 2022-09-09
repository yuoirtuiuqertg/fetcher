package ru.ais.hpsfetcher.model

import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Product", schema = "hypershop", catalog = "MSSQLApt2")
class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    var id: Int? = null

    @Column(name = "FetchRequestId", nullable = false)
    var fetchRequestId: Int? = null

    @Column(name = "StockId", nullable = false, length = 255)
    var stockId: String? = null

    @Column(name = "StockName", nullable = false, length = 255)
    var stockName: String? = null

    @Column(name = "ProducerId", nullable = true, length = 255)
    var producerId: String? = null

    @Column(name = "ProducerName", nullable = true, length = 255)
    var producerName: String? = null

    @Column(name = "StockGroup1Id", nullable = true, length = 255)
    var stockGroup1Id: String? = null

    @Column(name = "StockGroup1Name", nullable = true, length = 255)
    var stockGroup1Name: String? = null

    @Column(name = "Price1", nullable = true)
    var price1: String? = null

    @Column(name = "QtyForPrice1", nullable = true)
    var qtyForPrice1: Int? = 1

    @Column(name = "Price2", nullable = true)
    var price2: Float? = null

    @Column(name = "QtyForPrice2", nullable = true)
    var qtyForPrice2: Int? = null

    @Column(name = "CreateDateTime", nullable = false)
    var createDateTime: ZonedDateTime = ZonedDateTime.now()

    @Column(name = "WebPageLink", nullable = true, length = 255)
    var webPageLink: String? = null

    @Column(name = "Image1Link", nullable = true, length = 255)
    var image1Link: String? = null

    @Column(name = "Reserv1", nullable = true, length = 255)
    var reserv1: String? = null

    @Column(name = "Reserv2", nullable = true, length = 255)
    var reserv2: String? = null

    @Column(name = "Reserv3", nullable = true, length = 255)
    var reserv3: String? = null

    @Column(name = "Reserv4", nullable = true, length = 2147483647)
    var reserv4: String? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Product
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (fetchRequestId != null) fetchRequestId != that.fetchRequestId else that.fetchRequestId != null) return false
        if (if (stockId != null) stockId != that.stockId else that.stockId != null) return false
        if (if (stockName != null) stockName != that.stockName else that.stockName != null) return false
        if (if (producerId != null) producerId != that.producerId else that.producerId != null) return false
        if (if (producerName != null) producerName != that.producerName else that.producerName != null) return false
        if (if (stockGroup1Id != null) stockGroup1Id != that.stockGroup1Id else that.stockGroup1Id != null) return false
        if (if (stockGroup1Name != null) stockGroup1Name != that.stockGroup1Name else that.stockGroup1Name != null) return false
        if (if (price1 != null) price1 != that.price1 else that.price1 != null) return false
        if (if (qtyForPrice1 != null) qtyForPrice1 != that.qtyForPrice1 else that.qtyForPrice1 != null) return false
        if (if (price2 != null) price2 != that.price2 else that.price2 != null) return false
        if (if (qtyForPrice2 != null) qtyForPrice2 != that.qtyForPrice2 else that.qtyForPrice2 != null) return false
        if (createDateTime != that.createDateTime) return false
        if (if (webPageLink != null) webPageLink != that.webPageLink else that.webPageLink != null) return false
        if (if (image1Link != null) image1Link != that.image1Link else that.image1Link != null) return false
        if (if (reserv1 != null) reserv1 != that.reserv1 else that.reserv1 != null) return false
        if (if (reserv2 != null) reserv2 != that.reserv2 else that.reserv2 != null) return false
        if (if (reserv3 != null) reserv3 != that.reserv3 else that.reserv3 != null) return false
        return if (if (reserv4 != null) reserv4 != that.reserv4 else that.reserv4 != null) false else true
    }

    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 0
        result = 31 * result + if (fetchRequestId != null) fetchRequestId.hashCode() else 0
        result = 31 * result + if (stockId != null) stockId.hashCode() else 0
        result = 31 * result + if (stockName != null) stockName.hashCode() else 0
        result = 31 * result + if (producerId != null) producerId.hashCode() else 0
        result = 31 * result + if (producerName != null) producerName.hashCode() else 0
        result = 31 * result + if (stockGroup1Id != null) stockGroup1Id.hashCode() else 0
        result = 31 * result + if (stockGroup1Name != null) stockGroup1Name.hashCode() else 0
        result = 31 * result + if (price1 != null) price1.hashCode() else 0
        result = 31 * result + if (qtyForPrice1 != null) qtyForPrice1.hashCode() else 0
        result = 31 * result + if (price2 != null) price2.hashCode() else 0
        result = 31 * result + if (qtyForPrice2 != null) qtyForPrice2.hashCode() else 0
        result = 31 * result + createDateTime.hashCode()
        result = 31 * result + if (webPageLink != null) webPageLink.hashCode() else 0
        result = 31 * result + if (image1Link != null) image1Link.hashCode() else 0
        result = 31 * result + if (reserv1 != null) reserv1.hashCode() else 0
        result = 31 * result + if (reserv2 != null) reserv2.hashCode() else 0
        result = 31 * result + if (reserv3 != null) reserv3.hashCode() else 0
        result = 31 * result + if (reserv4 != null) reserv4.hashCode() else 0
        return result
    }
}