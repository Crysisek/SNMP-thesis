package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class, starts context.
 * [SecurityAutoConfiguration] is excluded to turn of the default security settings.
 *
 * @author kacper.kalinowski
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
public class ServerApplication {

  /**
   * Starts execution of the program.
   *
   * @param args passed to spring run method.
   */
  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

}
