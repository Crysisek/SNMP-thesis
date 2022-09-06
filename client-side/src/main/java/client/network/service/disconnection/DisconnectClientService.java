package client.network.service.disconnection;

/**
 * Responsible for disconnecting client from the server.
 *
 * @author kacper.kalinowski
 */
public interface DisconnectClientService {

  /**
   * Sends request to disconnect itself from the server.
   */
  void disconnect();
}
