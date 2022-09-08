package client.network.service.disconnection;

import java.util.UUID;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import client.network.dto.ClientResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import client.network.service.config.security.SecurityAuth;

/**
 * Implementation of DisconnectClientService.
 *
 * @author kacper.kalinowski
 */
@RequiredArgsConstructor
@Service
@Slf4j
class DisconnectClientServiceImpl implements DisconnectClientService {

  private final RestTemplate restTemplate;
  private final SecurityAuth securityAuth;
  private final ClientResponseDto clientResponseDto;

  @Value("${url.disconnect}")
  private String url;
  @Value("${auth.name}")
  private String name;
  @Value("${auth.password}")
  private String password;

  @PreDestroy
  public void disconnect() {
    restTemplate.exchange(
        url,
        HttpMethod.POST,
        new HttpEntity<>(clientResponseDto.getUuid(),
        securityAuth.createHeaders(name, password)),
        UUID.class
    );
    log.info("Client has been disconnected.");
  }
}
