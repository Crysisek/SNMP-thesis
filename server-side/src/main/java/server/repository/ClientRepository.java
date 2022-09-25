package server.repository;

import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import server.model.Client;

@Repository
public interface ClientRepository extends MongoRepository<Client, UUID> {

}
