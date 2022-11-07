package client.exception;

/**
 * Exception when registering to server fails.
 *
 * @author kacper.kalinowski
 */
public class FailedToRegisterException extends RuntimeException {

  /**
   * Construct custom client exception when registration is not successful.
   *
   * @param message Custom message.
   */
  public FailedToRegisterException(String message) {
    super(message);
  }
}
