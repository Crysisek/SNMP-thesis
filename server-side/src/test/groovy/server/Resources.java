package server;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import server.model.Client;
import server.model.ClientDetails;
import server.model.Status;
import server.types.ClientRole;
import server.types.Condition;

public final class Resources {

  public static final UUID TEST_ID = UUID.fromString("17c3ba38-203c-4584-a6b1-b0d0ce02736f");
  public static final UUID FAKE_ID = UUID.fromString("17c3ba38-203c-4584-a6b1-b0d0ce027333");
  public static final UUID DEFAULT_ID = UUID.fromString("22222222-2222-2222-2222-22222222222");
  public static final UUID ADMIN_ID = UUID.fromString("11111111-1111-1111-1111-11111111111");

  public static final List<Status> STATUSES = List.of(
      new Status(),
      new Status()
  );

  public static final List<Status> ONE_STATUS = List.of(
      new Status()
  );

  public static final Client TEST_CLIENT = Client.builder()
      .username(TEST_ID)
      .condition(Condition.NO_CONDITION)
      .createdAt(Instant.parse("2000-04-06T12:12:00.00Z"))
      .latestUpdateAt(Instant.parse("2003-04-06T12:12:00.00Z"))
      .build();

  public static final Client DEFAULT_CLIENT = Client.builder()
      .username(DEFAULT_ID)
      .role(ClientRole.DEFAULT)
      .createdAt(Instant.now())
      .latestUpdateAt(Instant.EPOCH)
      .condition(Condition.NO_CONDITION)
      .build();

  public static final Client ADMIN_CLIENT = Client.builder()
      .username(ADMIN_ID)
      .role(ClientRole.ADMIN)
      .createdAt(Instant.now())
      .latestUpdateAt(Instant.EPOCH)
      .condition(Condition.NO_CONDITION)
      .build();

  public static final List<String> RESPONSES = List.of(
      "{\"uuid\":\"28be7c08-2c09-48db-bbaf-c60fe6864519\",\"nameStatusPair\":{\"sysName\":\"Hardware: Intel64 Family 6 Model 94 Stepping 3 AT/AT COMPATIBLE - Software: Windows Version 6.3 (Build 19042 Multiprocessor Free)\",\"sysInfo\":\"WS-JPQYDC2.corp.smtsoftware.com\",\"sysUpTime\":\"5:12:31.47\",\"cpuUsage\":\"29.5 %\",\"usedSize\":\"104 GB\",\"maxSize\":\"237 GB\"}}\n",
      "{\"uuid\":\"28be7c08-2c09-48db-bbaf-c60fe6864519\",\"nameStatusPair\":{\"sysName\":\"Hardware: Intel64 Family 6 Model 94 Stepping 3 AT/AT COMPATIBLE - Software: Windows Version 6.3 (Build 19042 Multiprocessor Free)\",\"sysInfo\":\"WS-JPQYDC2.corp.smtsoftware.com\",\"sysUpTime\":\"5:12:31.47\",\"cpuUsage\":\"29.5 %\",\"usedSize\":\"104 GB\",\"maxSize\":\"237 GB\"}}\n"
  );
}
