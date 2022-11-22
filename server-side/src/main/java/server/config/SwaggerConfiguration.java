package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Responsible for setting up swagger.
 *
 * @author kacper.kalinowski
 */
@Configuration
public class SwaggerConfiguration {

//  @Bean
//  public Docket swaggerApi() {
//    return new Docket(DocumentationType.SWAGGER_2)
//        .select()
//        .paths(PathSelectors.regex("^(?!/(error).*$).*$"))
//        .build();
//  }
}
