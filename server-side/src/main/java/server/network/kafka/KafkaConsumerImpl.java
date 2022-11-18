package server.network.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import server.model.Status;
import server.network.dto.ClientResponseDto;
import server.service.ClientService;
import server.service.StatusService;
import server.tools.mappers.ClientMapper;
import server.types.Condition;

/**
 * Implementation of {@link server.network.kafka.KafkaConsumer}.
 *
 * @author kacper.kalinowski
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerImpl implements KafkaConsumer {

  private final ClientService clientService;

  private final StatusService statusService;

  private final ClientMapper clientMapper;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @KafkaListener(
      topics = {"${app.kafka.consumer.topic}"},
      groupId = "ListeningStatusesFromClients",
      concurrency = "${app.kafka.consumer.thread-count}"
  )
  public void receive(List<String> responses) {
    List<Status> statuses = extractFromStringsToResponseDtos(responses).stream()
        .map(clientMapper::toStatus)
        .toList();
    List<UUID> clientsUsernames = statuses.stream()
        .map(status -> status.getClient().getUsername())
        .toList();

    clientService.updateAllConditionsAndLatestUpdate(clientsUsernames, Condition.ONLINE, Instant.now());
    statusService.saveAll(statuses);
    log.info(statuses.size() + " known clients send their statuses and they were saved to the database.");
  }

  private List<ClientResponseDto> extractFromStringsToResponseDtos(List<String> responses) {
    List<ClientResponseDto> clientResponseDtos = new ArrayList<>();
    for (String response : responses) {
      try {
        ClientResponseDto responseDto = objectMapper.readValue(response, ClientResponseDto.class);
        if (clientService.existsById(responseDto.getUuid())) {
          clientResponseDtos.add(responseDto);
        }
      } catch (JsonProcessingException e) {
        log.warn(e.getMessage(), e);
      }
    }
    return clientResponseDtos;
  }
}
