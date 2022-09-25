package client.network.service.security;

import java.nio.charset.StandardCharsets;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Implementation of SecurityAuth.
 *
 * @author kacper.kalinowski
 */
@Component
class SecurityAuthImpl implements SecurityAuth {

  @Override
  public HttpHeaders createHeaders(String username, String password) {
    HttpHeaders httpHeaders = new HttpHeaders();
    String auth = username + ":" + password;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
    String authHeader = "Basic " + new String(encodedAuth);
    httpHeaders.set("Authorization", authHeader);
    return httpHeaders;
  }
}