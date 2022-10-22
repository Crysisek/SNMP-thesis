package server.network.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents pagination info.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

  private int currentPage;

  private int totalPages;

  private int currentPageSize;

  private long totalElements;
}
