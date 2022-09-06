package client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Responsible for creating rest template bean.
 *
 * @author kacper.kalinowski
 */
@Configuration
public class RestTemplateConfiguration {

  /**
   * Creation of rest template bean.
   *
   * @return RestTemplate.
   */
  @Bean
  public RestTemplate createRestTemplate() {
    return new RestTemplate();
  }
}
