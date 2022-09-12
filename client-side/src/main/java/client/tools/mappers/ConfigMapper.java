package client.tools.mappers;

import client.model.Config;
import client.network.dto.ConfigResponseDto;
import org.mapstruct.Mapper;

/**
 * Helper class for mapping config related objects.
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper {

  /**
   * Maps config dto to config model.
   *
   * @param configDto to be mapped from.
   * @return Config model object.
   */
  Config toConfig(ConfigResponseDto configDto);
}
