package client.network.service.kafka;

import client.network.dto.ClientResponseDto;

/**
 * Responsible for sending messages to the kafka server.
 *
 * @author kacper.kalinowski
 */
public interface KafkaProducerService {

  /**
   * Sends given dtos to the kafka.
   *
   * @param response Message to be sent.
   */
  void send(ClientResponseDto response);
}
