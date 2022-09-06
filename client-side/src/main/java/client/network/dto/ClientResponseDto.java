package client.network.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents message structure send to server-side by kafka.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor @AllArgsConstructor
public class ClientResponseDto {

  private UUID uuid;

  private Map<String, String> nameStatusPair;
}
