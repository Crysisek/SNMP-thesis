package server.service


import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import server.model.Status
import server.network.dto.StatusPresentationPagedDto
import server.repository.StatusRepository
import server.tools.mappers.StatusMapper
import server.tools.mappers.StatusMapperImpl
import server.types.SortColumn
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

import static server.Resources.ONE_STATUS
import static server.Resources.STATUSES
import static server.Resources.TEST_ID

class StatusServiceTest extends Specification {

    StatusMapper statusMapper = new StatusMapperImpl()

    StatusRepository statusRepository = Mock()

    @Subject
    StatusService statusService = new StatusServiceImpl(statusRepository, statusMapper)

    def "should return paged statuses with given id"() {
        when: "invoking get-method from service with given id"
        StatusPresentationPagedDto statusPresentationPagedDto = statusService.getStatusesByUserId(
                TEST_ID,
                1,
                10,
                null,
                null,
                SortColumn.CONDITION.toString(),
                Sort.Direction.ASC
        )
        then: "two statuses are expected in dto"
        statusPresentationPagedDto != null
        statusPresentationPagedDto.statuses.size() == 2
        1 * statusRepository.findByClientIdAndBetweenReceivingTime(TEST_ID, _ as Instant, _ as Instant, _ as PageRequest) >> {
            UUID id, Instant dateFrom, Instant dateTo, PageRequest pageRequest ->
                pageRequest.getPageNumber() == 1 ?
                        new PageImpl<Status>(STATUSES, PageRequest.of(1, 10), 10) : Page.empty()
        }
    }

    def "should return existing statuses between time interval"() {
        when: "searching for statuses between 1999-2000"
        StatusPresentationPagedDto statusPresentationPagedDto = statusService.getStatusesByUserId(
                TEST_ID,
                1,
                10,
                Instant.parse("1999-01-01T00:00:00.00Z"),
                Instant.parse("2001-01-01T00:00:00.00Z"),
                SortColumn.CONDITION.toString(),
                Sort.Direction.ASC
        )
        then: "getting one status"
        statusPresentationPagedDto != null
        statusPresentationPagedDto.statuses.size() == 1
        1 * statusRepository.findByClientIdAndBetweenReceivingTime(TEST_ID, _ as Instant, _ as Instant, _ as PageRequest) >> {
            UUID id, Instant dateFrom, Instant dateTo, PageRequest pageRequest -> new PageImpl<Status>(ONE_STATUS, PageRequest.of(1, 1), 1)
        }
    }

    def "should return empty list between given time interval"() {
        when: "searching for statuses between 1800-1850"
        StatusPresentationPagedDto statusPresentationPagedDto = statusService.getStatusesByUserId(
                TEST_ID,
                1,
                10,
                Instant.parse("1800-01-01T00:00:00.00Z"),
                Instant.parse("1850-01-01T00:00:00.00Z"),
                SortColumn.CONDITION.toString(),
                Sort.Direction.ASC
        )
        then: "getting empty list"
        statusPresentationPagedDto != null
        statusPresentationPagedDto.statuses.size() == 0
        1 * statusRepository.findByClientIdAndBetweenReceivingTime(TEST_ID, _ as Instant, _ as Instant, _ as PageRequest) >> {
            UUID id, Instant dateFrom, Instant dateTo, PageRequest pageRequest -> new PageImpl<Status>(List.of(), PageRequest.of(1, 1), 1)
        }
    }

    def "should save given status with usage of statusRepository"() {
        when: "saving status with statusService"
        statusService.save(ONE_STATUS[0])
        then: "one save method from statusRepository should be used"
        1 * statusRepository.save(ONE_STATUS[0]) >> ONE_STATUS[0]
    }

    def "should save list of given statuses with usage of statusRepository"() {
        when:
        statusService.saveAll(STATUSES)
        then:
        1 * statusRepository.saveAll(STATUSES) >> STATUSES
    }
}
