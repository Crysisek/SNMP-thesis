package server.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import server.model.Client;
import server.types.ClientRole;
import server.types.Condition;

@Repository
@RequiredArgsConstructor
public class ClientCustomRepositoryImpl implements ClientCustomRepository {

  private final MongoTemplate mongoTemplate;

  @Value("${schedule.inactivityPeriod}")
  private String inactivityPeriod;

  @Override
  public void updateCondition(UUID username, Condition condition) {
    mongoTemplate.update(Client.class)
        .matching(Criteria.where("_id").is(username))
        .apply(new Update().set(Condition.class.getSimpleName(), condition))
        .first();
  }

  @Override
  public void updateAllConditionsAndLatestUpdate(
      List<UUID> usernames,
      Condition condition,
      Instant now
  ) {
    mongoTemplate.update(Client.class)
        .matching(Criteria.where("_id").in(usernames))
        .apply(
            new Update().set(Condition.class.getSimpleName(), condition).set("latestUpdateAt", now)
        )
        .all();
  }

  @Override
  public void updateInactiveClientsConditions(Condition condition) {
    mongoTemplate.update(Client.class)
        .matching(
            Criteria.where("latestUpdateAt")
                .lt(Instant.now().minus(Long.parseLong(inactivityPeriod), ChronoUnit.MINUTES))
                .and(Condition.class.getSimpleName())
                .is(Condition.ONLINE)
        )
        .apply(new Update().set(Condition.class.getSimpleName(), condition))
        .all();
  }

  @Override
  public Page<Client> findByConditionInAndRoleIn(
      List<Condition> conditions,
      List<ClientRole> roles,
      Instant lastConnectionSince,
      Instant lastConnectionTill,
      Instant createdSince,
      Instant createdTill,
      Pageable pageable
  ) {
    Query query = Query.query(
        Criteria.where("condition").in(conditions)
            .and("role").in(roles)
            .and("latestUpdateAt")
            .gte(lastConnectionSince)
            .lte(lastConnectionTill)
            .and("createdAt")
            .gte(createdSince)
            .lte(createdTill)
    ).with(pageable);
    List<Client> clients = mongoTemplate.find(query, Client.class);
    return PageableExecutionUtils.getPage(clients, pageable,
        () -> mongoTemplate.count((Query.of(query).limit(-1).skip(-1)), Client.class));
  }
}
