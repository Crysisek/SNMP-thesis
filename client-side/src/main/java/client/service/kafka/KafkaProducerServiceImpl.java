package client.service.kafka;

import client.network.dto.ClientResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link KafkaProducerService}.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
@Slf4j
class KafkaProducerServiceImpl implements KafkaProducerService {

  private final KafkaTemplate<String, ClientResponseDto> kafkaTemplate;

  @Value("${app.kafka.producer.topic}")
  private String topic;

  @Override
  public void send(ClientResponseDto response) {
    log.info(response.getUuid() + "is publishing to topic " + topic);
    this.kafkaTemplate.send(topic, response);
  }
}
