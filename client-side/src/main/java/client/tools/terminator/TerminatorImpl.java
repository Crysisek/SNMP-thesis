package client.tools.terminator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TerminatorImpl implements Terminator {

  private final ApplicationContext applicationContext;

  @Override
  public Object terminate(Exception e, int status) {
    log.error("Application was terminated", e);
    SpringApplication.exit(applicationContext, () -> status);
    System.exit(status);
    return null;
  }
}
