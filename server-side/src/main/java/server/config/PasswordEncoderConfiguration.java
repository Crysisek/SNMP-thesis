package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Responsible for password decoding/encoding.
 *
 * @author kacper.kalinowski
 */
@Configuration
public class PasswordEncoderConfiguration {

  /**
   * Bean creation of PasswordEncoder.
   *
   * @return PasswordEncoder responsible for decoding/encoding.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(8);
  }
}
