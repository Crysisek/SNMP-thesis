package client.network.service.registration;

import java.io.File;
import client.network.dto.ClientResponseDto;

/**
 * Responsible for registration process.
 *
 * @author kacper.kalinowski
 */
public interface RegisterService {

  /**
   * Reading file to set clients id.
   *
   * @param file File to be read.
   * @return ClientResponseDto.
   */
  ClientResponseDto register(File file);
}
