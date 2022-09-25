package server.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import server.types.ClientRole;
import server.types.Condition;

@Data
@Builder
@Document
public class Client {

  @Id private UUID username;

  private String password;

  private ClientRole role;

  private Instant createdAt;

  private Instant latestUpdateAt;

  private Condition condition;
}
