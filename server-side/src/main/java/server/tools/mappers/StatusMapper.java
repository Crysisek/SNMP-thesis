package server.tools.mappers;

import org.mapstruct.Mapper;
import server.model.Status;
import server.network.dto.StatusPresentationDto;

/**
 * Helper class for mapping status related objects.
 *
 * @author kacper.kalinowski
 */
@Mapper(componentModel = "spring")
public interface StatusMapper {

  StatusPresentationDto toStatusPresentationDto(Status status);
}