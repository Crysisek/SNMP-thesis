package client.service.registration

import client.exception.FailedToRegisterException
import client.network.dto.ClientResponseDto
import client.service.security.SecurityAuth
import client.tools.terminator.Terminator
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

import static client.Resources.DOWNLOADED_UUID
import static client.Resources.HTTP_CLIENT_HEADER
import static client.Resources.REGISTER_FILE_EXISTS
import static client.Resources.REGISTER_FILE_NOT_EXISTS
import static client.Resources.TEST_UUID

class RegisterServiceTest extends Specification {

    RestTemplate restTemplate = Mock()

    SecurityAuth securityAuth = Mock()

    Terminator terminator = Mock()

    @Subject
    RegisterService registerService = new RegisterServiceImpl(
            restTemplate, securityAuth, terminator
    )

    def "should return uuid from file"() {
        when: "register method is performed on existing file"
        ClientResponseDto responseDto = registerService.register(REGISTER_FILE_EXISTS)
        then: "returned uuid is the same as one within file"
        "\"" + responseDto.uuid.toString() + "\"" == REGISTER_FILE_EXISTS.getText()
    }

    def "should download uuid from the server when file does not exist"() {
        given: "server url to register, and defaultId"
        ReflectionTestUtils.setField(registerService, "url", "http://test:8080/configuration/register")
        ReflectionTestUtils.setField(registerService, "defaultId", TEST_UUID.toString())
        when: "register method is performed"
        ClientResponseDto responseDto = registerService.register(REGISTER_FILE_NOT_EXISTS)
        then: "downloaded and saved id equals that inside file"
        responseDto.uuid == DOWNLOADED_UUID.getBody()
        1 * securityAuth.createHeaders(TEST_UUID.toString(), TEST_UUID.toString()) >> HTTP_CLIENT_HEADER
        1 * restTemplate.exchange(
                _ as String, _ as HttpMethod, _ as HttpEntity, _ as Class) >> DOWNLOADED_UUID
    }

    def "should throw exception when server is down"() {
        given: "url to register on down server, and defaultId"
        ReflectionTestUtils.setField(registerService, "url", "http://test:8080/configuration/register")
        ReflectionTestUtils.setField(registerService, "defaultId", TEST_UUID.toString())
        when: "register method is called"
        ClientResponseDto responseDto = registerService.register(REGISTER_FILE_NOT_EXISTS)
        then: "exception is thrown"
        thrown(FailedToRegisterException)
        1 * securityAuth.createHeaders(TEST_UUID.toString(), TEST_UUID.toString()) >> HTTP_CLIENT_HEADER
        1 * restTemplate.exchange(
                _ as String, _ as HttpMethod, _ as HttpEntity, _ as Class) >> { throw new RestClientException("error") }
    }

    def cleanup() {
        REGISTER_FILE_NOT_EXISTS.delete()
    }
}
