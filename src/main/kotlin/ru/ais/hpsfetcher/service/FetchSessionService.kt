package ru.ais.hpsfetcher.service

import org.springframework.stereotype.Service
import ru.ais.hpsfetcher.model.DataSource
import ru.ais.hpsfetcher.model.FetchSession
import ru.ais.hpsfetcher.model.enum.FetchSessionStatusEnum
import ru.ais.hpsfetcher.repository.FetchSessionRepository
import java.time.ZonedDateTime

@Service
class FetchSessionService(
    private val fetchSessionRepository: FetchSessionRepository,
    ) {

    fun createSession(dataSource: DataSource) = FetchSession()
        .let { session ->
            session.dataSourceId = dataSource.id
            session.status = FetchSessionStatusEnum.ACTIVE.id
            session.startDateTime = ZonedDateTime.now()
            fetchSessionRepository.save(session)
        }
}
