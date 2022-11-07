package server.repository;

import java.util.List;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import server.model.Client;
import server.model.Status;

/**
 * Responsible for communicating with the database.
 * Actions are carried out on {@link server.model.Status}.
 *
 * @author kacper.kalinowski
 */
@Repository
public interface StatusRepository extends MongoRepository<Status, ObjectId>, StatusCustomRepository {

  /**
   * Find all paginated statuses from given client.
   *
   * @param client which will be searched.
   * @return Paginated list of statuses matching the requirements.
   */
  List<Status> findAllByClient(Client client, Pageable pageable);
}
