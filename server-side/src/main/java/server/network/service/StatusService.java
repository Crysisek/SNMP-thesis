package server.network.service;

import java.util.List;
import server.model.Status;

public interface StatusService {

  Status save(Status status);

  List<Status> saveAll(List<Status> statuses);
}
