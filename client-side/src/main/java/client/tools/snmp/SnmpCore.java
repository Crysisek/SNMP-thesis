package client.tools.snmp;

import client.model.Config;
import client.network.dto.ClientResponseDto;

/**
 * Responsible for getting statuses from machine using Simple Network Management Protocol.
 *
 * @author kacper.kalinowski
 */
public interface SnmpCore {

  /**
   * This method downloads statuses based on provided OIDs from device.
   *
   * @param config OIDs that will be replaced by corresponding values.
   * @return ClientResponseDto.
   */
  ClientResponseDto downloadStatuses(Config config);
}
