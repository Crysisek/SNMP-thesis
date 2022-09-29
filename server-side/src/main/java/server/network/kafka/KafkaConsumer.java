package server.network.kafka;

import java.util.List;

/**
 * Responsible for receiving messages from the kafka server.
 *
 * @author kacper.kalinowski
 */
public interface KafkaConsumer {

  /**
   * Obtains messages from kafka topic.
   *
   * @param responses Batch of messages from kafka topic.
   */
  void receive(List<String> responses);
}
