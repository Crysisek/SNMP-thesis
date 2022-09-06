package client.network.kafka;

import lombok.RequiredArgsConstructor;
import client.model.Config;
import client.network.dto.ClientResponseDto;
import client.network.service.kafka.KafkaProducerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import client.tools.snmp.SnmpCore;

/**
 * Implementation of KafkaProducer.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
class KafkaProducerImpl implements KafkaProducer {

  private final KafkaProducerService kafkaProducerService;

  private final SnmpCore snmp;

  private final Config config;

  @ConditionalOnBean(Config.class)
  @Scheduled(fixedRateString = "#{@readConfig.timeInterval}")
  public void sendResponse() {
    ClientResponseDto response = snmp.downloadStatuses(config);
    kafkaProducerService.send(response);
  }
}
