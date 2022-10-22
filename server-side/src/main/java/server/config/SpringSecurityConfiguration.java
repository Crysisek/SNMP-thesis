package server.config;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import server.model.Client;
import server.network.service.ClientDetailsService;
import server.network.service.ClientService;
import server.types.ClientRole;
import server.types.Condition;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfiguration {

  private final ClientService clientService;

  private final PasswordEncoder passwordEncoder;

  private final ClientDetailsService clientDetailsService;

  @Value("${auth.register.username}")
  private String defaultId;

  @Value("${auth.admin.username}")
  private String adminId;

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(clientDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);

    return authProvider;
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .httpBasic().and()
        .formLogin().and()
        .authorizeRequests()
        .antMatchers("/api/configuration/register").hasAuthority(ClientRole.DEFAULT.name())
        .antMatchers("/api/configuration/config").hasAuthority(ClientRole.USER.name())
        .antMatchers("/api/configuration/disconnect/**").hasAuthority(ClientRole.USER.name())
        .antMatchers("/**").hasAnyAuthority(ClientRole.ADMIN.name());

    return http.build();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createDefaultClient() {
    Client defaultClient = Client.builder()
            .username(UUID.fromString(defaultId))
            .password(passwordEncoder.encode(defaultId))
            .role(ClientRole.DEFAULT)
            .createdAt(Instant.now())
            .latestUpdateAt(Instant.EPOCH)
            .condition(Condition.NO_CONDITION)
            .build();

    Client admin = Client.builder()
        .username(UUID.fromString(adminId))
        .password(passwordEncoder.encode(adminId))
        .role(ClientRole.ADMIN)
        .createdAt(Instant.now())
        .latestUpdateAt(Instant.EPOCH)
        .condition(Condition.NO_CONDITION)
        .build();

    if (clientService.findByUsername(defaultClient.getUsername()).isEmpty()) {
      clientService.save(defaultClient);
    }
    if (clientService.findByUsername(admin.getUsername()).isEmpty()) {
      clientService.save(admin);
    }
  }
}
