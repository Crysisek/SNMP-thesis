package server.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort.Direction;
import server.model.Status;
import server.network.dto.StatusPresentationDto;
import server.network.dto.StatusPresentationPagedDto;

/**
 * Responsible for operations on {@link server.model.Status}.
 *
 * @author kacper.kalinowski
 */
public interface StatusService {

  Status save(Status status);

  List<Status> saveAll(List<Status> statuses);

  List<StatusPresentationDto> getThreeNewestStatusesByUserId(UUID id);

  StatusPresentationPagedDto getStatusesByUserId(
      UUID clientId,
      int page,
      int size,
      Instant dateFrom,
      Instant dateTo,
      String sortColumn,
      Direction sortDirection
  );
}
