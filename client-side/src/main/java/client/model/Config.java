package client.model;

import java.util.Map;

/**
 * Represents configuration downloaded from server.
 *
 * @param timeInterval Time between each status sent.
 * @param nameOidPair Map holding name of the status with corresponding OIDs.
 * @author kacper.kalinowski
 */
public record Config(int timeInterval, Map<String, String> nameOidPair) {

}
