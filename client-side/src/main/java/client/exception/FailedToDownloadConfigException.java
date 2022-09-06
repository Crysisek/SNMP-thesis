package client.exception;

/**
 * Exception when downloading configuration from server fails.
 *
 * @author kacper.kalinowski
 */
public class FailedToDownloadConfigException extends RuntimeException {

  /**
   * Construct custom client.exception when download is not successful.
   *
   * @param message Custom message.
   */
  public FailedToDownloadConfigException(String message) {
    super(message);
  }
}
