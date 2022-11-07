package client.config;

import client.exception.FailedToDownloadConfigException;
import client.exception.FailedToRegisterException;
import client.model.ClientInstance;
import client.model.Config;
import client.network.dto.ClientResponseDto;
import client.network.dto.ConfigResponseDto;
import client.network.service.config.ConfigurationDownloadService;
import client.network.service.registration.RegisterService;
import client.tools.mappers.ClientMapper;
import client.tools.mappers.ConfigMapper;
import client.tools.terminator.Terminator;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * Responsible for setting starting configuration, for entire app.
 *
 * @author kacper.kalinowski
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
class LaunchingConfiguration {

  @Lazy
  private final ConfigurationDownloadService configurationDownloadService;

  private final RegisterService registerService;

  private final ConfigMapper configMapper;

  private final ClientMapper clientMapper;

  private final Terminator terminator;

  @Value("${configfile.path}")
  private String configFilePath;

  @Value("${registerfile.path}")
  private String registerFilePath;

  /**
   * Bean creation of type {@link client.model.Config}.
   *
   * @return Downloaded or read configuration.
   */
  @Bean
  @Retryable(value = {FailedToDownloadConfigException.class},
      maxAttemptsExpression = "${retry.config.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.config.delay}")
  )
  public Config readConfig() {
    ConfigResponseDto clientConfiguration = configurationDownloadService.getClientConfiguration(
        Paths.get(configFilePath).toFile()
    );
    return configMapper.toConfig(clientConfiguration);
  }

  /**
   * Bean creation of type {@link client.model.ClientInstance}.
   *
   * @return Downloaded or read Client data.
   */
  @Bean
  @Retryable(value = {FailedToRegisterException.class},
      maxAttemptsExpression = "${retry.register.maxAttempts}",
      backoff = @Backoff(delayExpression = "${retry.register.delay}")
  )
  public ClientInstance register() {
    ClientResponseDto clientResponseDto = registerService.register(Paths.get(registerFilePath).toFile());
    return clientMapper.toClient(clientResponseDto);
  }

  @Recover
  private ClientResponseDto recoverAfterFailingToRegister(FailedToRegisterException e) {
    log.error(
        "After many attempts, application could not register to the server, and will now shut down."
    );
    return (ClientResponseDto) terminator.terminate(e, 1);
  }

  @Recover
  private Config recoverAfterFailingToDownloadConfig(FailedToDownloadConfigException e) {
    log.error(
        "After many attempts, application could not download configuration from the server, and now will shut down."
    );
    return (Config) terminator.terminate(e, 2);
  }
}
