package client.network.service.config;

import client.exception.FailedToDownloadConfigException;
import client.model.ClientInstance;
import client.network.dto.ConfigResponseDto;
import client.network.service.security.SecurityAuth;
import client.tools.terminator.Terminator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of ConfigurationDownloadService.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
@Slf4j
class ConfigurationDownloadServiceImpl implements ConfigurationDownloadService {

  private final RestTemplate restTemplate;

  private final SecurityAuth securityAuth;

  private final ClientInstance client;

  private final Terminator terminator;

  @Value("${url.config}")
  private String url;

  @Override
  public ConfigResponseDto getClientConfiguration(File configFile) {
    this.getClientConfigurationFromServer(configFile);

    ConfigResponseDto config = new ConfigResponseDto();
    try {
      ObjectMapper mapper = new ObjectMapper();
      config = mapper.readValue(configFile, ConfigResponseDto.class);
    } catch (IOException e) {
      terminator.terminate(e, 5);
    }
    return config;
  }

  private void getClientConfigurationFromServer(File file) {
    if (file.length() == 0) {
      try {
        ConfigResponseDto config = restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(securityAuth.createHeaders(client.uuid().toString(), client.uuid().toString())),
            ConfigResponseDto.class
        ).getBody();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, config);
      } catch (RestClientException e) {
        log.warn("Failed to download configuration from server.");
        throw new FailedToDownloadConfigException("Failed to download configuration from server.");
      } catch (IOException e) {
        terminator.terminate(e, 5);
      }
    }
  }
}
