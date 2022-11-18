package client.service.security;

import org.springframework.http.HttpHeaders;

/**
 * Responsible for security matters.
 *
 * @author kacper.kalinowski
 */
public interface SecurityAuth {

  /**
   * Create header with provided username, and password.
   *
   * @param username Username to be used.
   * @param password Password to be used.
   * @return HttpHeader.
   */
  HttpHeaders createHeaders(String username, String password);
}