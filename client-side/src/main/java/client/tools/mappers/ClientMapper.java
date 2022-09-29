package client.tools.mappers;

import client.model.ClientInstance;
import client.network.dto.ClientResponseDto;
import org.mapstruct.Mapper;

/**
 * Helper class for mapping client related objects.
 *
 * @author kacper.kalinowski
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

  /**
   * Maps client dto to client model.
   *
   * @param clientDto to be mapped from.
   * @return Client model object.
   */
  ClientInstance toClient(ClientResponseDto clientDto);
}
