package client.exception;

/**
 * Exception when application can't connect to SNMP client.service.
 *
 * @author kacper.kalinowski
 */
public class SnmpException extends RuntimeException {

  /**
   * Construct custom client.exception when connection time to SNMP is exceeded.
   *
   * @param message Custom message.
   */
  public SnmpException(String message) {
    super(message);
  }
}
