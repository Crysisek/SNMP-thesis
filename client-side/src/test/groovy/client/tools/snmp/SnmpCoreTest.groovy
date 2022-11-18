package client.tools.snmp

import client.model.ClientInstance
import client.network.dto.ClientResponseDto
import client.tools.snmp.SnmpCore
import client.tools.snmp.SnmpImpl
import client.tools.terminator.Terminator
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification
import spock.lang.Subject

import static client.Resources.FAKE_CONFIG_DATA
import static client.Resources.MOCK_CONFIG_DATA
import static client.Resources.TEST_UUID

class SnmpCoreTest extends Specification {

    ClientInstance clientInstance = new ClientInstance(TEST_UUID);

    @Subject
    SnmpCore snmp = new SnmpImpl(clientInstance, Mock(Terminator));

    def "should return clientResponseDto with 4 values"() {
        given: "object created/init method called"
        ReflectionTestUtils.invokeMethod(snmp, "init")
        when: "calling method start with valid argument"
        ClientResponseDto clientResponseDto = snmp.downloadStatuses(MOCK_CONFIG_DATA)
        then: "method returns clientResponseDto"
        !clientResponseDto.nameStatusPair.isEmpty()
        clientResponseDto.nameStatusPair.size() == MOCK_CONFIG_DATA.nameOidPair().size() - 1
    }

    def "should return clientResponseDto with 4 'noSuchObject' values when OIDS are unknown"() {
        given: "object created/init method called"
        ReflectionTestUtils.invokeMethod(snmp, "init")
        when: "calling method start with invalid OIDS"
        ClientResponseDto clientResponseDto = snmp.downloadStatuses(FAKE_CONFIG_DATA)
        then: "map with 'noSuchObject' is produced"
        !clientResponseDto.nameStatusPair.isEmpty()
        clientResponseDto.nameStatusPair.size() == FAKE_CONFIG_DATA.nameOidPair().size()
        clientResponseDto.nameStatusPair.get("fake1") == "noSuchObject"
    }
}
