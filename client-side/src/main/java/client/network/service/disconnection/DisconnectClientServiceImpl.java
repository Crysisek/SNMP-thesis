package client.network.service.disconnection;

import client.model.ClientInstance;
import client.network.service.security.SecurityAuth;
import java.util.UUID;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

  private final ClientInstance client;

  @Value("${url.disconnect}")
  private String url;

  @PreDestroy
  public void disconnect() {
    restTemplate.exchange(
        url,
        HttpMethod.POST,
        new HttpEntity<>(securityAuth.createHeaders(client.uuid().toString(), client.uuid().toString())),
        UUID.class,
        client.uuid()
    );
    log.info("Client has been disconnected.");
  }
}
