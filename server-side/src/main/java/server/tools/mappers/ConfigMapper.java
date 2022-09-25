package server.tools.mappers;

import org.mapstruct.Mapper;
import server.model.Config;
import server.network.dto.ConfigResponseDto;

/**
 * Helper class for mapping config related objects.
 *
 * @author kacper.kalinowski
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper {

  /**
   * Maps config model to config dto.
   *
   * @param config to be mapped from.
   * @return Config dto object.
   */
  ConfigResponseDto toConfigResponseDto(Config config);
}
