package server.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort.Direction;
import server.model.Client;
import server.network.dto.ClientPresentationDto;
import server.network.dto.ClientPresentationPagedDto;
import server.types.ClientRole;
import server.types.Condition;
import server.types.SortColumn;

/**
 * Responsible for operations on {@link server.model.Client}.
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
   * Searches for client by username.
   *
   * @param username Provided username.
   * @return Optional of Client.
   */
  Optional<Client> findByUsername(UUID username);

  /**
   * Searches for client by username and maps result to DTO object.
   *
   * @param username Provided username.
   * @return mapped dto object.
   */
  ClientPresentationDto getClientByUsername(UUID username);

  /**
   * Searches for clients by given criteria.
   *
   * @param page number.
   * @param size of page.
   * @param lastConnectionSince earlier date.
   * @param lastConnectionTill later date.
   * @param createdSince earlier date.
   * @param createdTill later date.
   * @param conditions list of conditions to filter by.
   * @param sortColumn by which.
   * @param sortDirection ASC or DESC.
   * @return list of clients satisfying given criteria.
   */
  ClientPresentationPagedDto getClients(
      int page,
      int size,
      Instant lastConnectionSince,
      Instant lastConnectionTill,
      Instant createdSince,
      Instant createdTill,
      List<Condition> conditions,
      List<ClientRole> roles,
      SortColumn sortColumn,
      Direction sortDirection
  );
}
