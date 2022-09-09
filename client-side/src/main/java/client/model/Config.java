package client.model;

import java.util.Map;

/**
 * Represents configuration downloaded from server.
 *
 * @author kacper.kalinowski
 */
public record Config(int timeInterval, Map<String, String> nameOidPair) {

}
