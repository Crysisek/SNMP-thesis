package client.config;

import client.exception.FailedToDownloadConfigException;
import client.exception.FailedToRegisterException;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import client.model.Config;
import client.network.dto.ClientResponseDto;
import client.network.dto.ConfigResponseDto;
import client.network.service.config.ConfigurationDownloadService;
import client.network.service.registration.RegisterService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@Configuration
@RequiredArgsConstructor
@Slf4j
class StartingConfiguration {

  private final ConfigurationDownloadService configurationDownloadService;

  private final RegisterService registerService;

  private final ApplicationContext applicationContext;

  @Bean
  @Retryable(value = {FailedToDownloadConfigException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 5000))
  public Config readConfig() {
    ConfigResponseDto clientConfiguration = configurationDownloadService.getClientConfiguration(
        Paths.get("bin/client.config.json").toFile()
    );
    return clientConfiguration.toConfig();
  }

  @Bean
  @Retryable(value = {FailedToRegisterException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 5000))
  public ClientResponseDto register() {
    return registerService.register(Paths.get("bin/uuid.json").toFile());
  }

  @Recover
  private ClientResponseDto recoverAfterFailingToRegister(FailedToRegisterException e) {
    log.error(
        "After many attempts, application could not register to the server, and will now shut down.");
    SpringApplication.exit(applicationContext, () -> 1);
    System.exit(1);
    return null;
  }

  @Recover
  private Config recoverAfterFailingToDownloadConfig(FailedToDownloadConfigException e) {
    log.error(
        "After many attempts, application could not download configuration from the server, and now will shut down.");
    SpringApplication.exit(applicationContext, () -> 2);
    System.exit(2);
    return null;
  }
}
