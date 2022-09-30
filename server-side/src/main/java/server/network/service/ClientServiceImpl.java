package server.network.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.model.Client;
import server.repository.ClientRepository;
import server.types.ClientRole;
import server.types.Condition;

/**
 * Implementation of UserService.
 *
 * @author kacper.kalinowski
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final PasswordEncoder passwordEncoder;

  private final ClientRepository repository;

  @Override
  public Client save(Client client) {
    return repository.save(client);
  }

  public void updateCondition(UUID username, Condition condition) {
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

  @Scheduled(fixedDelayString = "${schedule.log.checkOfflineClients}", timeUnit = TimeUnit.MINUTES)
  void checkOfflineClients() {
    repository.findAllByCondition(Condition.OFFLINE).forEach(offline -> log.warn(
            "Client with id: {} did not send any statuses for the last {} minutes. Latest update was: {}",
            offline.getUsername(),
            offline.getLatestUpdateAt().until(Instant.now(), ChronoUnit.MINUTES),
            offline.getLatestUpdateAt()
        )
    );
  }

  @Scheduled(fixedDelayString = "${schedule.markOfflineClients}", timeUnit = TimeUnit.MINUTES)
  void markOfflineClients() {
    repository.updateInactiveClientsConditions(Condition.OFFLINE);
  }
}
