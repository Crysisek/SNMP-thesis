package server.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServerHealthIndicator implements HealthIndicator, ApplicationListener<ApplicationReadyEvent> {

  private volatile boolean isAppReady;

  @Override
  public Health health() {
    Health.Builder status;
    if (isAppReady) {
      status = Health.up();
    } else {
      status = Health.down();
    }
    return status
        .withDetail("Service", "SNMP-server")
        .build();
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    isAppReady = true;
  }
}
