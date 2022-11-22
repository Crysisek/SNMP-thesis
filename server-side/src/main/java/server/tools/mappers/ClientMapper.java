package server.tools.mappers;

import java.time.Instant;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import server.model.Client;
import server.model.Status;
import server.network.dto.ClientPresentationDto;
import server.network.dto.ClientResponseDto;

/**
 * Helper class for mapping client related objects.
 *
 * @author kacper.kalinowski
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

  /**
   * Maps Client to ClientPresentationDto.
   *
   * @param client Parameter to be mapped from.
   * @return ClientPresentationDto.
   */
  ClientPresentationDto toClientPresentationDto(Client client);

  /**
   * Maps ClientResponseDto to Status.
   *
   * @param dto Parameter to be mapped from.
   * @return Status.
   */
  @Mappings({
      @Mapping(source = "uuid", target = "client", qualifiedByName = "idToClient"),
      @Mapping(source = "uuid", target = "receivingTime", qualifiedByName = "instantNow")
  })
  Status toStatus(ClientResponseDto dto);

  @Named("idToClient")
  static Client idToClient(UUID id) {
    return Client.builder().username(id).build();
  }

  @Named("instantNow")
  static Instant instantNow(UUID id) {
    return Instant.now();
  }
}
