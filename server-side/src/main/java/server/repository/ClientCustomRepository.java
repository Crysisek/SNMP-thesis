package server.repository;

import java.util.UUID;

public interface ClientCustomRepository {

  void disconnectUserIfExists(UUID username);
}
