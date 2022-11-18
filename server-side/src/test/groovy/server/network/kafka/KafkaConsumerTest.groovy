package server.network.kafka


import server.service.ClientService
import server.service.StatusService
import server.tools.mappers.ClientMapper
import server.tools.mappers.ClientMapperImpl
import spock.lang.Specification
import spock.lang.Subject

import static server.Resources.RESPONSES

class KafkaConsumerTest extends Specification {

    ClientService clientService = Mock()

    StatusService statusService = Mock()

    ClientMapper clientMapper = new ClientMapperImpl()

    @Subject
    KafkaConsumer kafkaConsumer = new KafkaConsumerImpl(clientService, statusService, clientMapper)

    def "should update clients and save all statuses"() {
        when: "kafka consumer receives responses"
        kafkaConsumer.receive(RESPONSES)
        then: "3 methods should be invoked"
        RESPONSES.size() * clientService.existsById(_)
        1 * clientService.updateAllConditionsAndLatestUpdate(_, _, _)
        1 * statusService.saveAll(_)
    }
}
