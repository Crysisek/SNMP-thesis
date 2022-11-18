package client.service.disconnection


import client.model.ClientInstance
import client.service.security.SecurityAuth
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

import static client.Resources.HTTP_CLIENT_HEADER
import static client.Resources.TEST_UUID

class DisconnectClientServiceTest extends Specification {

    RestTemplate restTemplate = Mock()

    SecurityAuth securityAuth = Mock()

    ClientInstance clientInstance = new ClientInstance(TEST_UUID)

    @Subject
    DisconnectClientService disconnectClientService = new DisconnectClientServiceImpl(
            restTemplate, securityAuth, clientInstance
    )

    def "should use restTemplate method to send request to disconnect"() {
        given: "server url to disconnect from"
        ReflectionTestUtils.setField(disconnectClientService, "url", "http://test:8080/configuration/disconnect")
        when: "disconnect method is performed"
        disconnectClientService.disconnect()
        then: "exchange is performed"
        1 * securityAuth.createHeaders(TEST_UUID.toString(), TEST_UUID.toString()) >> HTTP_CLIENT_HEADER
        1 * restTemplate.exchange(
                _ as String, _ as HttpMethod, _ as HttpEntity, _ as Class, _ as Object)
    }
}
