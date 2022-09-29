package server.network.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.model.Status;
import server.repository.StatusRepository;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

  private final StatusRepository repository;

  @Override
  public Status save(Status status) {
    return repository.save(status);
  }

  @Override
  public List<Status> saveAll(List<Status> statuses) {
    return repository.saveAll(statuses);
  }
}
