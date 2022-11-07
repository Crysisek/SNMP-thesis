package server.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import server.model.Client;
import server.types.ClientRole;
import server.types.Condition;

/**
 * Responsible for expanding {@link server.repository.ClientRepository} functionality.
 *
 * @author kacper.kalinowski
 */
public interface ClientCustomRepository {

  /**
   * Updates condition of client with given ID.
   *
   * @param username ID of the client.
   * @param condition to be updated.
   */
  void updateCondition(UUID username, Condition condition);

  /**
   * Updates conditions and latest update of all clients with matching IDs.
   *
   * @param usernames list of clients IDs.
   * @param condition to be updated.
   * @param now time of the latest update to be updated.
   */
  void updateAllConditionsAndLatestUpdate(List<UUID> usernames, Condition condition, Instant now);

  /**
   * Updates condition of all the clients if latest update is less than inactivity period.
   *
   * @param condition most of the time {@link server.types.Condition#OFFLINE}.
   */
  void updateInactiveClientsConditions(Condition condition);

  /**
   * Retrieve paginated result of provided criteria.
   *
   * @param conditions list of conditions to filter by.
   * @param roles list of roles to filter by.
   * @param lastConnectionSince earlier date.
   * @param lastConnectionTill later date.
   * @param createdSince earlier date.
   * @param createdTill later date.
   * @param pageable representing what page to retrieve.
   * @return page of clients matching those criteria.
   */
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
