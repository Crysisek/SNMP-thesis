package client.network.dto;

import client.model.Config;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents configuration dto downloaded from server.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigResponseDto {

  private int timeInterval;

  private Map<String, String> nameOidPair;
}