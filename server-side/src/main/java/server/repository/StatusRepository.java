package server.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import server.model.Status;

@Repository
public interface StatusRepository extends MongoRepository<Status, ObjectId> {

}
