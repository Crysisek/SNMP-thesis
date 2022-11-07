package server.network.controller;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.network.dto.StatusPresentationPagedDto;
import server.network.service.StatusService;

/**
 * Sets endpoints related to {@link server.model.Status}.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class StatusController {

  private final StatusService statusService;

  /**
   * Retrieve {@link server.network.dto.StatusPresentationPagedDto}.
   *
   * @param clientId which statuses will be shown.
   * @param page of request.
   * @param size of page.
   * @param dateFrom time of send statuses from.
   * @param dateTo time of send statuses till.
   * @param sortColumn sort by column.
   * @param sortDirection sort direction.
   * @return Page with statuses of provided clientId, meeting the requirements.
   */
  @GetMapping("statuses/{clientId}")
  public ResponseEntity<StatusPresentationPagedDto> getStatuses(
      @PathVariable UUID clientId,
      @RequestParam(value = "pageNo", defaultValue = "0") int page,
      @RequestParam(value = "pageSize", defaultValue = "20") int size,
      @RequestParam(defaultValue = "") Instant dateFrom,
      @RequestParam(defaultValue = "") Instant dateTo,
      @RequestParam(value = "sortBy", defaultValue = "receivingTime") String sortColumn,
      @RequestParam(value = "sortDir", defaultValue = "DESC") Direction sortDirection
  ) {
    return new ResponseEntity<>(
        statusService.getStatusesByUserId(
            clientId, page, size,
            dateFrom, dateTo,
            sortColumn, sortDirection
        ),
        HttpStatus.OK
    );
  }
}
