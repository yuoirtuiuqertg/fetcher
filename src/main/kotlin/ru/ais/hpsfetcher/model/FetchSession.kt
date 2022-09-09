package ru.ais.hpsfetcher.model

import java.sql.Date
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "FetchSession", schema = "hypershop", catalog = "MSSQLApt2")
class FetchSession {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    var id: Int? = null

    @Column(name = "StartDateTime", nullable = false)
    var startDateTime: ZonedDateTime? = null

    @Column(name = "FinishDateTime", nullable = true)
    var finishDateTime: Date? = null

    @Column(name = "Status", nullable = true)
    var status: Int? = null

    @Column(name = "DataSourceId", nullable = false)
    var dataSourceId: Int? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as FetchSession
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (startDateTime != null) startDateTime != that.startDateTime else that.startDateTime != null) return false
        if (if (finishDateTime != null) finishDateTime != that.finishDateTime else that.finishDateTime != null) return false
        if (if (status != null) status != that.status else that.status != null) return false
        return if (if (dataSourceId != null) dataSourceId != that.dataSourceId else that.dataSourceId != null) false else true
    }

    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 0
        result = 31 * result + if (startDateTime != null) startDateTime.hashCode() else 0
        result = 31 * result + if (finishDateTime != null) finishDateTime.hashCode() else 0
        result = 31 * result + if (status != null) status.hashCode() else 0
        result = 31 * result + if (dataSourceId != null) dataSourceId.hashCode() else 0
        return result
    }
}