package server.network.controller;

import java.time.Instant;
import java.util.List;
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
import server.network.dto.ClientPresentationDto;
import server.network.dto.ClientPresentationPagedDto;
import server.network.service.ClientService;
import server.types.ClientRole;
import server.types.Condition;
import server.types.SortColumn;

/**
 * Sets endpoints related to {@link server.model.Client}.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ClientController {

  private final ClientService clientService;


  @GetMapping("client/{username}")
  public ResponseEntity<ClientPresentationDto> getClient(@PathVariable UUID username) {
    ClientPresentationDto clientPresentationDto = clientService.getClientByUsername(username);
    if (clientPresentationDto != null) {
      return new ResponseEntity<>(clientPresentationDto, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("clients")
  public ResponseEntity<ClientPresentationPagedDto> getClients(
      @RequestParam(value = "pageNo", defaultValue = "0") int page,
      @RequestParam(value = "pageSize", defaultValue = "20") int size,
      @RequestParam(defaultValue = "") Instant lastConnectionSince,
      @RequestParam(defaultValue = "") Instant lastConnectionTill,
      @RequestParam(defaultValue = "") Instant createdSince,
      @RequestParam(defaultValue = "") Instant createdTill,
      @RequestParam(defaultValue = "") List<Condition> conditions,
      @RequestParam(defaultValue = "") List<ClientRole> roles,
      @RequestParam(value = "sortBy", defaultValue = "CONDITION") SortColumn sortColumn,
      @RequestParam(value = "sortDir", defaultValue = "ASC") Direction sortDirection
  ) {
    return new ResponseEntity<>(
        clientService.getClients(
            page, size, lastConnectionSince, lastConnectionTill,
            createdSince, createdTill, conditions,
            roles, sortColumn, sortDirection
        ),
        HttpStatus.OK
    );
  }
}
