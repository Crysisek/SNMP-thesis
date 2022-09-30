package server.network.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import server.model.Client;
import server.types.Condition;

/**
 * Responsible for operations on [Client] model.
 *
 * @author kacper.kalinowski
 */
public interface ClientService {

  /**
   * Save client into database.
   *
   * @param client Client to be saved.
   * @return Saved client.
   */
  Client save(Client client);

  /**
   * Update client condition in database to provided condition.
   *
   * @param username of client to be updated.
   * @param condition updated condition.
   */
  void updateCondition(UUID username, Condition condition);

  /**
   * Update clients conditions in database to provided condition.
   *
   * @param usernames of clients to be updated.
   * @param condition updated condition.
   * @param now current time.
   */
  void updateAllConditionsAndLatestUpdate(List<UUID> usernames, Condition condition, Instant now);

  /**
   * Returns whether a client with the given username exists.
   *
   * @param username to be checked.
   * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
   */
  boolean existsById(UUID username);

  /**
   * Create client.
   * @return Created client.
   */
  Client createClient();

  /**
   * Searches for customer by username.
   *
   * @param username Provided username.
   * @return Optional of Client.
   */
  Optional<Client> findByUsername(UUID username);
}
