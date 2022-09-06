package client.network.kafka;

/**
 * Responsible for creating and sending messages to kafka server.
 *
 * @author kacper.kalinowski
 */
public interface KafkaProducer {

  /**
   * Sends message to kafka.
   */
  void sendResponse();
}
