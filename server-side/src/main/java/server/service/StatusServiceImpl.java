package server.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import server.model.Client;
import server.model.Status;
import server.network.dto.PageDto;
import server.network.dto.StatusPresentationDto;
import server.network.dto.StatusPresentationPagedDto;
import server.repository.StatusRepository;
import server.tools.mappers.StatusMapper;

/**
 * Implementation of {@link StatusService}.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

  private final StatusRepository repository;

  private final StatusMapper statusMapper;

  @Override
  public Status save(Status status) {
    return repository.save(status);
  }

  @Override
  public List<Status> saveAll(List<Status> statuses) {
    return repository.saveAll(statuses);
  }

  @Override
  public List<StatusPresentationDto> getThreeNewestStatusesByUserId(UUID id) {
    Sort sort = Sort.by(Direction.DESC, "receivingTime");
    Pageable pageable = PageRequest.of(0, 3, sort);

    return repository.findAllByClient(
            Client.builder()
                .username(id)
                .build(),
            pageable
        ).stream()
        .map(statusMapper::toStatusPresentationDto)
        .toList();
  }

  @Override
  public StatusPresentationPagedDto getStatusesByUserId(
      UUID clientId, int page, int size,
      Instant dateFrom, Instant dateTo,
      String sortColumn, Direction sortDirection
  ) {
    Sort sort = Sort.by(sortDirection, sortColumn);
    Pageable pageable = PageRequest.of(page, size, sort);

    if (dateFrom == null) {
      dateFrom = Instant.EPOCH;
    }
    if (dateTo == null) {
      dateTo = Instant.now();
    }

    Page<Status> statuses = repository.findByClientIdAndBetweenReceivingTime(
        clientId,
        dateFrom,
        dateTo,
        pageable
    );

    List<StatusPresentationDto> statusPresentationDtos = statuses.get()
        .map(statusMapper::toStatusPresentationDto)
        .toList();

    return StatusPresentationPagedDto.builder()
        .statuses(statusPresentationDtos)
        .pageInfo(new PageDto(
            statuses.getPageable().getPageNumber(),
            statuses.getTotalPages(),
            statuses.getSize(),
            statuses.getTotalElements()
        ))
        .build();
  }
}
