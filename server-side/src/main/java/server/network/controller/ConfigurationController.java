package server.network.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.model.Client;
import server.model.Config;
import server.network.dto.ConfigResponseDto;
import server.network.service.ClientService;
import server.tools.mappers.ConfigMapper;
import server.types.Condition;

/**
 * Sets endpoints for registration, config download and disconnection.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/configuration")
public class ConfigurationController {

  private final Config config;

  private final ConfigMapper configMapper;

  private final ClientService clientService;

  @PostMapping("register")
  public ResponseEntity<UUID> register() {
    Client newClient = clientService.createClient();
    clientService.save(newClient);
    return new ResponseEntity<>(newClient.getUsername(), HttpStatus.CREATED);
  }

  @GetMapping("config")
  public ResponseEntity<ConfigResponseDto> downloadConfig() {
    return new ResponseEntity<>(configMapper.toConfigResponseDto(config), HttpStatus.OK);
  }

  @PostMapping("disconnect/{username}")
  public void disconnect(@PathVariable UUID username) {
    clientService.updateCondition(username, Condition.DISCONNECTED);
  }
}
