package client.network.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import client.exception.FailedToDownloadConfigException;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import client.network.dto.ConfigResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import client.tools.security.SecurityAuth;

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

  @Value("${url.config}")
  private String url;
  @Value("${auth.name}")
  private String name;
  @Value("${auth.password}")
  private String password;

  @Override
  public ConfigResponseDto getClientConfiguration(File configFile) {
    this.getClientConfigurationFromServer(configFile);

    ConfigResponseDto config = new ConfigResponseDto();
    try {
      ObjectMapper mapper = new ObjectMapper();

      config = mapper.readValue(configFile, ConfigResponseDto.class);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    return config;
  }

  private void getClientConfigurationFromServer(File file) {
    if (file.length() == 0) {
      try {
        ConfigResponseDto config = restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(securityAuth.createHeaders(this.name, this.password)),
            ConfigResponseDto.class
        ).getBody();
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(file, config);
      } catch (RestClientException e) {
        log.warn("Failed to download configuration from server.");
        throw new FailedToDownloadConfigException("Failed to download configuration from server.");
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }
  }
}
