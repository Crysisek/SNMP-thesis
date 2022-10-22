package server.network.dto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents status exposed by api.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPresentationDto {

  private Instant receivingTime;

  private Map<String, String> nameStatusPair = new HashMap<>();
}
