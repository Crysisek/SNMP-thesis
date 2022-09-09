package client.network.service.registration;

import client.network.dto.ClientResponseDto;
import java.io.File;

/**
 * Responsible for registration process.
 *
 * @author kacper.kalinowski
 */
public interface RegisterService {

  /**
   * Register client to the server. If already registered, read id from file.
   *
   * @param file File to be read.
   * @return ClientResponseDto.
   */
  ClientResponseDto register(File file);
}
