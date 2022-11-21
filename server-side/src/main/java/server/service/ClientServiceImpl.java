package server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.model.Client;
import server.network.dto.ClientPresentationDto;
import server.network.dto.ClientPresentationPagedDto;
import server.network.dto.PageDto;
import server.repository.ClientRepository;
import server.tools.mappers.ClientMapper;
import server.types.ClientRole;
import server.types.Condition;
import server.types.SortColumn;

/**
 * Implementation of {@link ClientService}.
 *
 * @author kacper.kalinowski
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final PasswordEncoder passwordEncoder;

  private final ClientRepository repository;

  private final StatusService statusService;

  private final ClientMapper clientMapper;

  @Override
  public Client save(Client client) {
    return repository.save(client);
  }

  public void updateCondition(UUID username, Condition condition) {
    log.info("Setting condition of username with id: {} to Condition: {}", username, condition);
    repository.updateCondition(username, condition);
  }

  @Override
  public void updateAllConditionsAndLatestUpdate(
      List<UUID> usernames,
      Condition condition,
      Instant now
  ) {
    repository.updateAllConditionsAndLatestUpdate(usernames, condition, now);
  }

  @Override
  public boolean existsById(UUID username) {
    return repository.existsById(username);
  }

  @Override
  public Client createClient() {
    UUID username = UUID.randomUUID();
    log.info("Creating user with id: " + username);
    return Client.builder()
        .username(username)
        .password(passwordEncoder.encode(username.toString()))
        .role(ClientRole.USER)
        .createdAt(Instant.now())
        .condition(Condition.OFFLINE)
        .build();
  }

  @Override
  public Optional<Client> findByUsername(UUID id) {
    return repository.findById(id);
  }

  @Override
  public ClientPresentationDto getClientByUsername(UUID username) {
    Optional<Client> client = findByUsername(username);
    return client.map(clientMapper::toClientPresentationDto).orElse(null);
  }

  @Override
  public ClientPresentationPagedDto getClients(
      int page, int size, Instant lastConnectionSince,
      Instant lastConnectionTill, Instant createdSince, Instant createdTill,
      List<Condition> conditions, List<ClientRole> roles, SortColumn sortColumn,
      Direction sortDirection
  ) {
    Sort sort = Sort.by(sortDirection, sortColumn.getParam());
    Pageable pageable = PageRequest.of(page, size, sort);

    if (roles.isEmpty()) {
      roles = List.of(ClientRole.values());
    }
    if (conditions.isEmpty()) {
      conditions = List.of(Condition.values());
    }
    if (lastConnectionSince == null) {
      lastConnectionSince = Instant.EPOCH;
    }
    if (lastConnectionTill == null) {
      lastConnectionTill = Instant.now();
    }
    if (createdSince == null) {
      createdSince = Instant.EPOCH;
    }
    if (createdTill == null) {
      createdTill = Instant.now();
    }

    Page<Client> clients = repository.findByConditionInAndRoleIn(
        conditions,
        roles,
        lastConnectionSince,
        lastConnectionTill,
        createdSince,
        createdTill,
        pageable
    );

    List<ClientPresentationDto> clientPresentationDtos = clients.get()
        .map(clientMapper::toClientPresentationDto)
        .peek(clientPresentationDto ->
            clientPresentationDto.setStatusList(
                statusService.getThreeNewestStatusesByUserId(clientPresentationDto.getUsername())))
        .toList();

    return ClientPresentationPagedDto.builder()
        .clients(clientPresentationDtos)
        .pageInfo(new PageDto(
            clients.getPageable().getPageNumber(),
            clients.getTotalPages(),
            clients.getSize(),
            clients.getTotalElements()
        ))
        .build();
  }

  @Scheduled(fixedDelayString = "${schedule.log.checkOfflineClients}", timeUnit = TimeUnit.MINUTES)
  private void checkOfflineClients() {
    repository.findAllByCondition(Condition.OFFLINE).forEach(offline -> log.warn(
            "Client with id: {} did not send any statuses for the last {} minutes. Latest update was: {}",
            offline.getUsername(),
            offline.getLatestUpdateAt().until(Instant.now(), ChronoUnit.MINUTES),
            offline.getLatestUpdateAt()
        )
    );
  }

  @Scheduled(fixedDelayString = "${schedule.markOfflineClients}", timeUnit = TimeUnit.MINUTES)
  private void markOfflineClients() {
    repository.updateInactiveClientsConditions(Condition.OFFLINE);
    log.info("Updated inactive client conditions to " + Condition.OFFLINE);
  }
}
