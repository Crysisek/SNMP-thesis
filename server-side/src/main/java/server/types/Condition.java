package server.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Condition in which client can be.
 * @author kacper.kalinowski
 */
@lombok.Generated
@RequiredArgsConstructor
@Getter
public enum Condition {
  /**
   * Offline: when client did not send statuses for specified number of minutes.
   */
  OFFLINE("OFFLINE"),
  /**
   * Online: when client is actively sending statuses.
   */
  ONLINE("ONLINE"),
  /**
   * Disconnected: when client shuts itself down.
   */
  DISCONNECTED("DISCONNECTED");

  private final String param;
}