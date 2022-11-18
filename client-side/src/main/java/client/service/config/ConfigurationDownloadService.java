package client.service.config;

import client.network.dto.ConfigResponseDto;
import java.io.File;

/**
 * Responsible for configuration download.
 *
 * @author kacper.kalinowski
 */
public interface ConfigurationDownloadService {

  /**
   * Read client config from file. If file is empty or does not exist, try downloading configuration
   * from server.
   *
   * @param configFile File to be read.
   * @return ConfigDataDto.
   */
  ConfigResponseDto getClientConfiguration(File configFile);
}
