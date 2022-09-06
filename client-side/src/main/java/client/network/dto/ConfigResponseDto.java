package client.network.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import client.model.Config;

/**
 * Represents configuration dto downloaded from server.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor @AllArgsConstructor
public class ConfigResponseDto {

  private int timeInterval;

  private Map<String, String> nameOidPair;

  public Config toConfig() {
    return new Config(this.timeInterval, this.nameOidPair);
  }
}