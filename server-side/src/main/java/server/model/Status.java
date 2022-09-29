package server.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * Represents status send from client-side.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@Document
public class Status {

  private Instant receivingTime;

  private Map<String, String> nameStatusPair = new HashMap<>();

  @DocumentReference(lazy = true)
  private Client client;
}
