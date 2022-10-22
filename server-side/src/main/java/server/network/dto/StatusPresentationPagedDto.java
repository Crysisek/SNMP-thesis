package server.network.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents status exposed by api alongside pagination info.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPresentationPagedDto {

  private List<StatusPresentationDto> statuses;

  private PageDto pageInfo;
}
