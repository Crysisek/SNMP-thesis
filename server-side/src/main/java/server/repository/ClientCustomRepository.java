package server.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import server.types.Condition;

public interface ClientCustomRepository {

  void updateCondition(UUID username, Condition condition);

  void updateAllConditionsAndLatestUpdate(List<UUID> usernames, Condition condition, Instant now);

  void updateInactiveClientsConditions(Condition condition);
}
