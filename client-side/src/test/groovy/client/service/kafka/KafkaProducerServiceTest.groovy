package client.service.kafka


import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.util.concurrent.ListenableFuture
import spock.lang.Specification
import spock.lang.Subject

import static client.Resources.MOCK_CLIENT_RESPONSE
import static client.Resources.MOCK_KAFKA_TOPIC

class KafkaProducerServiceTest extends Specification {

    KafkaTemplate kafkaTemplate = Mock()

    @Subject
    KafkaProducerService kafkaProducerService = new KafkaProducerServiceImpl(kafkaTemplate)

    def "should successfully send data"() {
        given: "topic name of kafka cluster"
        ReflectionTestUtils.setField(kafkaProducerService, "topic", MOCK_KAFKA_TOPIC)
        ListenableFuture future = Mock()
        when: "kafka sends client response"
        kafkaProducerService.send(MOCK_CLIENT_RESPONSE)
        then: "kafka template sends client response on expected topic"
        1 * kafkaTemplate.send(MOCK_KAFKA_TOPIC, MOCK_CLIENT_RESPONSE) >> future
    }
}
