package server.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import server.model.Client;
import server.model.ClientDetails;

/**
 * Implementation of {@link org.springframework.security.core.userdetails.UserDetailsService}.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientDetailsService implements UserDetailsService {

  private final ClientService clientService;

  @Value("${auth.register.username}")
  private String defaultId;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Client client = clientService.findByUsername(UUID.fromString(username))
        .orElseThrow(() -> new UsernameNotFoundException(
            "User with given username: " + username + " does not exist.")
        );

    if (defaultId.equals(username)) {
      log.info("Client tried registering using default username.");
    } else {
      log.info(username + " tried to be authenticated.");
    }
    return new ClientDetails(client);
  }
}
