package server.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Roles specifying clients capabilities.
 * @author kacper.kalinowski
 */
@lombok.Generated
@AllArgsConstructor
@Getter
public enum ClientRole {
  /**
   * Admin: can connect to presentation api.
   */
  ADMIN("admin"),
  /**
   * User: for clients sending their data, access only to configuration api.
   */
  USER("user"),
  /**
   * Default: used only for registration process.
   */
  DEFAULT("default");

  private final String name;
}
