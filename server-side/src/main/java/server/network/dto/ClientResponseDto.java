package server.network.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents message structure received by server-side from kafka.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto {

  private UUID uuid;

  private Map<String, String> nameStatusPair;
}
