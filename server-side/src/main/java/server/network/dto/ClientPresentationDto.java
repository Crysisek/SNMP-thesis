package server.network.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.types.ClientRole;
import server.types.Condition;

/**
 * Represents client exposed by api.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPresentationDto {

  private UUID username;

  private ClientRole role;

  private Instant createdAt;

  private Instant latestUpdateAt;

  private Condition condition;

  private List<StatusPresentationDto> statusList;
}
