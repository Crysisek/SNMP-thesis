package server.repository;

import java.util.List;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import server.model.Client;
import server.model.Status;

@Repository
public interface StatusRepository extends MongoRepository<Status, ObjectId>, StatusCustomRepository {

  List<Status> findAllByClient(Client client, Pageable pageable);
}
