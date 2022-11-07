package server.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import server.model.Client;
import server.types.Condition;

/**
 * Responsible for communicating with the database.
 * Actions are carried out on {@link server.model.Client}.
 *
 * @author kacper.kalinowski
 */
@Repository
public interface ClientRepository extends MongoRepository<Client, UUID>, ClientCustomRepository {

  /**
   * Find all clients with given condition.
   *
   * @param condition which will be used to search.
   * @return List of clients matching the requirements.
   */
  List<Client> findAllByCondition(Condition condition);
}
