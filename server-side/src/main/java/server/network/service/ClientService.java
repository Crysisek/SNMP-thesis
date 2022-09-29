package server.network.service;

import java.util.Optional;
import java.util.UUID;
import server.model.Client;

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
   * Update clients condition to [Condition.Disconnected] in database.
   *
   * @param username of client to be updated.
   */
  void disconnect(UUID username);

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
