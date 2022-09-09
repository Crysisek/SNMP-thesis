package client.tools.terminator;

/**
 * Responsible for closing application when error occurs.
 *
 * @author kacper.kalinowski
 */
public interface Terminator {

  /**
   * This method downloads statuses based on provided OIDs from device.
   *
   * @param e Thrown exception.
   * @param status Exit status different from 0.
   * @return null.
   */
  Object terminate(Exception e, int status);
}
