package server.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import server.model.Client;
import server.types.ClientRole;
import server.types.Condition;

public interface ClientCustomRepository {

  void updateCondition(UUID username, Condition condition);

  void updateAllConditionsAndLatestUpdate(List<UUID> usernames, Condition condition, Instant now);

  void updateInactiveClientsConditions(Condition condition);

  Page<Client> findByConditionInAndRoleIn(
      List<Condition> conditions,
      List<ClientRole> roles,
      Instant lastConnectionSince,
      Instant lastConnectionTill,
      Instant createdSince,
      Instant createdTill,
      Pageable pageable
  );
}
