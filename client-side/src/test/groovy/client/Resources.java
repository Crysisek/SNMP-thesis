package client;

import client.model.Config;
import client.network.dto.ClientResponseDto;
import client.network.dto.ConfigResponseDto;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public final class Resources {

  public static final UUID TEST_UUID = UUID.fromString(
      "28be7c08-2c09-48db-ccaf-c60fe6174519");

  public static final int TEST_TIME_INTERVAL = 5000;

  public static final Map<String, String> TEST_OIDS = Map.of("sysInfo", ".1.3.6.1.2.1.1.5.0",
      "maxSize", ".1.3.6.1.2.1.25.2.3.1.5.1",
      "usedSize", ".1.3.6.1.2.1.25.2.3.1.6.1", "allocationUnit", ".1.3.6.1.2.1.25.2.3.1.4.1",
      "cpuUsage", ".1.3.6.1.2.1.25.3.3.1.2.5");

  public static final Map<String, String> FAKE_OIDS = Map.of("fake1", ".1.3.6.1.40.0.0.0.0",
      "fake2", ".1.3.6.1.40.0.0.0.0", "fake3", ".1.3.6.1.40.0.0.0.0", "fake4",
      ".1.3.6.1.40.0.0.0.0");

  public static final Map<String, String> TEST_STATUSES = null;

  public static final ConfigResponseDto MOCK_CONFIG_RESPONSE_DATA = new ConfigResponseDto(TEST_TIME_INTERVAL,
      TEST_OIDS);

  public static final Config MOCK_CONFIG_DATA = new Config(TEST_TIME_INTERVAL, TEST_OIDS);

  public static final Config FAKE_CONFIG_DATA = new Config(TEST_TIME_INTERVAL, FAKE_OIDS);

  public static final File REGISTER_FILE_EXISTS = Paths.get(
      "src/test/groovy/testFiles/testUuid.json").toFile();

  public static final File REGISTER_FILE_NOT_EXISTS = Paths.get(
      "src/test/groovy/testFiles/notTestUuid.json").toFile();

  public static final ResponseEntity DOWNLOADED_UUID = ResponseEntity.of(Optional.of(TEST_UUID));

  public static final HttpHeaders HTTP_CLIENT_HEADER = new HttpHeaders() {
    {
      String auth = "client:client";
      byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
      String authHeader = "Basic " + new String(encodedAuth);
      set("Authorization", authHeader);
    }
  };

  public static final HttpHeaders HTTP_ADMIN_HEADER = new HttpHeaders() {
    {
      String auth = "admin:admin";
      byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
      String authHeader = "Basic " + new String(encodedAuth);
      set("Authorization", authHeader);
    }
  };

  public static final File CONFIG_FILE_EXISTS = Paths.get(
      "src/test/groovy/testFiles/testConfig.json").toFile();

  public static final File CONFIG_FILE_NOT_EXISTS = Paths.get(
      "src/test/groovy/testFiles/notTestConfig.json").toFile();

  public static final ResponseEntity DOWNLOADED_CONFIG = ResponseEntity.of(
      Optional.of(MOCK_CONFIG_RESPONSE_DATA));

  public static final ClientResponseDto MOCK_CLIENT_RESPONSE = new ClientResponseDto(TEST_UUID,
      TEST_STATUSES);

  public static final String MOCK_KAFKA_TOPIC = "SendingValues";
}
