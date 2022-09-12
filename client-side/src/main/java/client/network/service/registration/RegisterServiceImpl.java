package client.network.service.registration;

import client.exception.FailedToRegisterException;
import client.network.dto.ClientResponseDto;
import client.network.service.config.security.SecurityAuth;
import client.tools.terminator.Terminator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of RegisterService.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
@Slf4j
class RegisterServiceImpl implements RegisterService {

  private final RestTemplate restTemplate;

  private final SecurityAuth securityAuth;

  private final Terminator terminator;

  @Value("${url.register}")
  private String url;

  @Value("${auth.name}")
  private String name;

  @Value("${auth.password}")
  private String password;

  @Override
  public ClientResponseDto register(File file) {
    this.registerToServer(file);

    ClientResponseDto client = new ClientResponseDto();
    try {
      ObjectMapper mapper = new ObjectMapper();
      UUID uuid = mapper.readValue(file, UUID.class);
      client.setUuid(uuid);
    } catch (IOException e) {
      terminator.terminate(e, 4);
    }
    return client;
  }

  private void registerToServer(File file) {
    if (file.length() == 0) {
      try {
        UUID uuid = restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(securityAuth.createHeaders(name, password)),
            UUID.class
        ).getBody();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, uuid);
      } catch (RestClientException e) {
        log.warn("Failed to register to server at address: " + url);
        throw new FailedToRegisterException("Failed to register to server at address: " + url);
      } catch (IOException e) {
        terminator.terminate(e, 4);
      }
    }
  }
}