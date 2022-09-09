package ru.ais.hpsfetcher.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.ais.hpsfetcher.model.FetchRequest
import ru.ais.hpsfetcher.repository.FetchRequestRepository
import java.time.ZonedDateTime

@Service
class FetchRequestService(
    private val fetchRequestRepository: FetchRequestRepository,
) {
    companion object {
        const val EXPIRY_PERIOD: Long = 10 * 60 * 60
    }

    internal fun loadResponseFromCache(url: String): FetchRequest? {
        val expiredDateTime = ZonedDateTime.now().minusSeconds(EXPIRY_PERIOD)
        val requestList = fetchRequestRepository.findCachedRequest(request = url, expiredDateTime)
        return requestList.firstOrNull()
    }

    internal fun saveResponseToCache(url: String, response: String?, httpStatus: HttpStatus) =
        FetchRequest().let {
            it.request = url
            it.response = response
            it.httpStatus = httpStatus.value()
            fetchRequestRepository.save(it)
        }
}
