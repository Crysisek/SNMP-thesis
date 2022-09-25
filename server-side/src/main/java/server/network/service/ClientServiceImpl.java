package server.network.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final PasswordEncoder passwordEncoder;

  private final ClientRepository repository;

  @Override
  public Client save(Client client) {
    return repository.save(client);
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
}
