package server.repository;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import server.model.Client;
import server.types.Condition;

@Repository
@RequiredArgsConstructor
public class ClientCustomRepositoryImpl implements ClientCustomRepository {

  private final MongoTemplate mongoTemplate;

  @Override
  public void disconnectUserIfExists(UUID username) {
    mongoTemplate.update(Client.class)
        .matching(Criteria.where("_id").is(username))
        .apply(new Update().set(Condition.class.getSimpleName(), Condition.DISCONNECTED))
        .first();
  }
}
