package client.tools.snmp;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import client.model.Config;
import client.network.dto.ClientResponseDto;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import client.exception.SnmpException;

/**
 * Implementation of SnmpCore.
 *
 * @author kacper.kalinowski
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SnmpImpl implements SnmpCore {

  private static final int GIGABYTE = 1073741824;
  private static final String NO_SUCH_OBJECT = "noSuchObject";
  private static final String CPU_USAGE = "cpuUsage";
  private static final String MAX_SIZE = "maxSize";
  private static final String USED_SIZE = "usedSize";
  private static final String ALLOCATION_UNIT = "allocationUnit";

  private final ClientResponseDto clientResponseDto;
  private final ApplicationContext applicationContext;
  private Snmp snmp = null;
  private CommunityTarget target;
  private int cores;

  @PostConstruct
  private void init() {
    try {
      this.snmp = new Snmp(new DefaultUdpTransportMapping());
      this.snmp.listen();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    this.target = new CommunityTarget();
    this.target.setCommunity(new OctetString("public"));
    this.target.setAddress(GenericAddress.parse("udp:localhost/161"));
    this.target.setRetries(2);
    this.target.setTimeout(1500);
    this.target.setVersion(SnmpConstants.version2c);

    cores = Runtime.getRuntime().availableProcessors();
  }

  @Override
  @Retryable(value = {SnmpException.class},
      maxAttemptsExpression = "${retry.snmp.maxAttepmts}",
      backoff = @Backoff(delayExpression = "${retry.snmp.delay}")
  )
  public ClientResponseDto downloadStatuses(Config config) {
    Map<String, String> results = new LinkedHashMap<>();
    for (String key : config.nameOidPair().keySet()) {
      String oid = config.nameOidPair().get(key);
      if (key.equals(CPU_USAGE) && !oid.equals(NO_SUCH_OBJECT)) {
        String lastChar = oid.substring(config.nameOidPair().get(key).length() - 1);
        String sequence = oid.substring(0, config.nameOidPair().get(key).length() - 1);
        String[] all = new String[cores];
        for (int i = 0; i < cores; ++i) {
          all[i] = sequence + (Integer.parseInt(lastChar) + i);
        }
        List<VariableBinding> cpusCoreUsage = this.getList(all);
        double result = cpusCoreUsage.stream()
            .mapToInt(v -> v.getVariable().toInt())
            .average()
            .orElse(0.0);
        results.put(key, (int) result + " %");
        continue;
      }
      try {
        results.put(key, this.get(oid).getVariable().toString());
      } catch (SnmpException e) {
        log.warn(e.getMessage(), e);
        throw e;
      }
    }

    if (!results.getOrDefault(ALLOCATION_UNIT, NO_SUCH_OBJECT).equals(NO_SUCH_OBJECT)
        || !results.getOrDefault(MAX_SIZE, NO_SUCH_OBJECT).equals(NO_SUCH_OBJECT)
        || !results.getOrDefault(USED_SIZE, NO_SUCH_OBJECT).equals(NO_SUCH_OBJECT)) {
      int allocationUnit = Integer.parseInt(results.remove("allocationUnit"));
      int maxSize = Integer.parseInt(results.remove(MAX_SIZE));
      int usedSize = Integer.parseInt(results.remove(USED_SIZE));

      long max = (long) maxSize * allocationUnit / GIGABYTE;
      long used = max - (long) usedSize * allocationUnit / GIGABYTE;

      results.put(USED_SIZE, used + " GB");
      results.put(MAX_SIZE, max + " GB");
    }

    clientResponseDto.setNameStatusPair(results);
    return clientResponseDto;
  }

  private VariableBinding get(String oid) {
    PDU pdu = new PDU();
    pdu.add(new VariableBinding(new OID(oid)));
    ResponseEvent response = null;
    try {
      response = snmp.send(pdu, this.target);
    } catch (IOException e) {
      log.error(e.getMessage());
    }

    if (response == null || response.getResponse() == null) {
      throw new SnmpException("SNMP response timed out!");
    } else {
      return response.getResponse().get(0);
    }
  }

  private List<VariableBinding> getList(String oids[]) {
    PDU pdu = new PDU();
    OID[] arrayOfOids = Arrays.stream(oids)
        .map(OID::new).toArray(OID[]::new);
    pdu.addAll(VariableBinding.createFromOIDs(arrayOfOids));
    ResponseEvent response = null;
    try {
      response = snmp.send(pdu, this.target);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    if (response == null || response.getResponse() == null) {
      throw new SnmpException("SNMP response timed out!");
    } else {
      return response.getResponse().getAll();
    }
  }

  @Recover
  private ClientResponseDto recoverAfterSnmpException(SnmpException e) {
    log.error("After many attempts, application could not connect to the SNMP client.service!");
    SpringApplication.exit(applicationContext, () -> 3);
    System.exit(3);
    return null;
  }
}
