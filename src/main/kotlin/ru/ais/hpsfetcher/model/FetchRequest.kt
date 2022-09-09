package ru.ais.hpsfetcher.model

import org.springframework.http.HttpStatus
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "FetchRequest", schema = "hypershop", catalog = "MSSQLApt2")
class FetchRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    var id: Int? = null

    @Column(name = "Request", nullable = true, length = 8000)
    var request: String? = null

    @Column(name = "Response", nullable = true, length = 2147483647)
    var response: String? = null

    @Column(name = "HttpStatus", nullable = false)
    var httpStatus: Int = HttpStatus.OK.value()

    @Column(name = "CreateDateTime", nullable = false)
    var createDateTime: ZonedDateTime = ZonedDateTime.now()

    @Column(name = "FetchSessionId", nullable = true)
    var fetchSessionId: Int? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as FetchRequest
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (request != null) request != that.request else that.request != null) return false
        if (if (response != null) response != that.response else that.response != null) return false
        return if (if (fetchSessionId != null) fetchSessionId != that.fetchSessionId else that.fetchSessionId != null) false else true
    }

    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 0
        result = 31 * result + if (request != null) request.hashCode() else 0
        result = 31 * result + if (response != null) response.hashCode() else 0
        result = 31 * result + if (fetchSessionId != null) fetchSessionId.hashCode() else 0
        return result
    }
}