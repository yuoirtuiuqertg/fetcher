package ru.ais.hpsfetcher.model

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "DataSourceType", schema = "hypershop", catalog = "MSSQLApt2")
class DataSourceType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    var id: Int? = null

    @Column(name = "Name", nullable = false, length = 255)
    var name: String? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DataSourceType
        if (if (id != null) id != that.id else that.id != null) return false
        return if (if (name != null) name != that.name else that.name != null) false else true
    }

    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 0
        result = 31 * result + if (name != null) name.hashCode() else 0
        return result
    }
}