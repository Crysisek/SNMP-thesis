package server.repository;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import server.model.Status;

/**
 * Responsible for expanding {@link server.repository.StatusRepository} functionality.
 *
 * @author kacper.kalinowski
 */
public interface StatusCustomRepository {

  /**
   * Retrieve paginated result of provided criteria.
   *
   * @param clientId of which the statuses will be from.
   * @param dateFrom earlier date of receiving status.
   * @param dateTo later date of receiving status.
   * @param pageable representing what page to retrieve.
   * @return page of statuses matching those criteria.
   */
  Page<Status> findByClientIdAndBetweenReceivingTime(
      UUID clientId,
      Instant dateFrom,
      Instant dateTo,
      Pageable pageable
  );
}
