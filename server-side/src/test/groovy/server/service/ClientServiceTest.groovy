package server.service


import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import server.model.Client
import server.Resources
import server.network.dto.ClientPresentationPagedDto
import server.repository.ClientRepository
import server.repository.StatusRepository
import server.tools.mappers.ClientMapper
import server.tools.mappers.ClientMapperImpl
import server.tools.mappers.StatusMapper
import server.tools.mappers.StatusMapperImpl
import server.types.ClientRole
import server.types.Condition
import server.types.SortColumn
import spock.lang.Specification
import spock.lang.Subject
import java.time.Instant

import static server.Resources.FAKE_ID
import static server.Resources.TEST_CLIENT
import static server.Resources.TEST_ID

class ClientServiceTest extends Specification {

    PasswordEncoder passwordEncoder = Mock()

    ClientRepository clientRepository = Mock()

    StatusMapper statusMapper = new StatusMapperImpl()

    StatusService statusService = new StatusServiceImpl(Mock(StatusRepository.class), statusMapper)

    ClientMapper clientMapper = new ClientMapperImpl()

    @Subject
    ClientService clientService = new ClientServiceImpl(passwordEncoder, clientRepository, statusService, clientMapper)

    def "should return client with given id"() {
        when: "getting client with given id"
        Optional<Client> client = clientService.findByUsername(TEST_ID)
        then: "client is not null"
        client.isPresent()
        client.get() == TEST_CLIENT
        1 * clientRepository.findById(_ as UUID) >> { UUID clientId ->
            TEST_CLIENT.username == clientId ? Optional.of(TEST_CLIENT) : Optional.empty()
        }
    }

    def "should return empty optional when client with given id is not found"() {
        when: "getting client with given id, that is not in database"
        Optional<Client> client = clientService.findByUsername(FAKE_ID)
        then: "return empty optional"
        client.isEmpty()
        1 * clientRepository.findById(FAKE_ID) >> Optional.empty()
    }

    def "should save given client using clientRepository"() {
        when: "saving client using clientService"
        clientService.save(TEST_CLIENT)
        then: "one clientRepository is called"
        1 * clientRepository.save(TEST_CLIENT) >> TEST_CLIENT
    }

    def "should update clients condition"() {
        when: "updating client's condition'"
        clientService.updateCondition(TEST_ID, Condition.DISCONNECTED)
        then: "clientRepository calls updateCondition with corresponding condition"
        1 * clientRepository.updateCondition(TEST_ID, Condition.DISCONNECTED)
    }

    def "should return empty list when there is no clients in database"() {
        when: "getting all clients when there is non in database"
        ClientPresentationPagedDto clientPresentationPagedDto = clientService.getClients(
                1,
                10,
                null,
                null,
                null,
                null,
                List.of(),
                List.of(),
                SortColumn.CONDITION,
                Sort.Direction.ASC
        )
        then: "receiving empty page"
        clientPresentationPagedDto.getClients().size() == 0
        clientPresentationPagedDto.getPageInfo().getTotalElements() == 0
        1 * clientRepository.findByConditionInAndRoleIn(
                _ as List,
                _ as List,
                _ as Instant,
                _ as Instant,
                _ as Instant,
                _ as Instant,
                _ as PageRequest
        ) >> new PageImpl<Client>(List.of(), PageRequest.of(1, 1), 0)
    }

    def "should return true if client with given id exists in database"() {
        when: "when searching for client with given id"
        boolean result = clientService.existsById(TEST_ID)
        then: "getting true with usage of existsById method"
        result
        1 * clientRepository.existsById(TEST_ID) >> true
    }

    def "should return false if client with given id does not exist in database"() {
        when: "when searching for client with given id"
        boolean result = clientService.existsById(TEST_ID)
        then: "getting false with usage of existsById method"
        !result
        1 * clientRepository.existsById(TEST_ID) >> false
    }

    def "should return newly created client"() {
        when: "creating new client"
        Client client = clientService.createClient()
        then: "client should have role and condition on default state"
        client.role == ClientRole.USER
        client.condition == Condition.OFFLINE
    }

    def "should return client presentation dto when client exists"() {
        when:
        def clientPresentationDto = clientService.getClientByUsername(Resources.TEST_ID)
        then:
        clientPresentationDto.username == TEST_CLIENT.username
        1 * clientRepository.findById(_ as UUID) >> { UUID clientId ->
            TEST_CLIENT.username == clientId ? Optional.of(TEST_CLIENT) : Optional.empty()
        }

    }
}
