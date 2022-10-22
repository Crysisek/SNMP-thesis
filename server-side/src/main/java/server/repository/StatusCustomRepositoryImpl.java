package server.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import server.model.Status;

@Repository
@RequiredArgsConstructor
public class StatusCustomRepositoryImpl implements StatusCustomRepository {

  private final MongoTemplate mongoTemplate;

  @Override
  public Page<Status> findByClientIdAndBetweenReceivingTime(
      UUID clientId,
      Instant dateFrom,
      Instant dateTo,
      Pageable pageable
  ) {
    Query query = Query.query(
        Criteria.where("client").is(clientId)
            .and("receivingTime")
            .gte(dateFrom)
            .lte(dateTo)
    ).with(pageable);
    List<Status> statuses = mongoTemplate.find(query, Status.class);
    return PageableExecutionUtils.getPage(statuses, pageable,
        () -> mongoTemplate.count((Query.of(query).limit(-1).skip(-1)), Status.class));
  }
}
