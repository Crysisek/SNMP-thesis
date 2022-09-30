package server.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import server.model.Client;
import server.types.Condition;

@Repository
public interface ClientRepository extends MongoRepository<Client, UUID>, ClientCustomRepository {

  List<Client> findAllByCondition(Condition condition);
}
