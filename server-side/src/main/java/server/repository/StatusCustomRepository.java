package server.repository;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import server.model.Status;

public interface StatusCustomRepository {

  Page<Status> findByClientIdAndBetweenReceivingTime(
      UUID clientId,
      Instant dateFrom,
      Instant dateTo,
      Pageable pageable
  );
}
