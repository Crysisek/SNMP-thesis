package server.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Column on which the sort will be performed.
 *
 * @author kacper.kalinowski
 */
@lombok.Generated
@RequiredArgsConstructor
@Getter
public enum SortColumn {
  USERNAME("_id"),

  ROLE("role"),

  CONDITION("condition"),

  LATEST_UPDATE("latestUpdateAt"),

  CREATED("createdAt");

  private final String param;
}
