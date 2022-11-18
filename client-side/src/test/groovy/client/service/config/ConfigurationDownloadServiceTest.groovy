package client.service.config

import client.exception.FailedToDownloadConfigException
import client.model.ClientInstance
import client.network.dto.ConfigResponseDto
import client.service.security.SecurityAuth
import client.tools.terminator.Terminator
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

import static client.Resources.CONFIG_FILE_EXISTS
import static client.Resources.CONFIG_FILE_NOT_EXISTS
import static client.Resources.DOWNLOADED_CONFIG
import static client.Resources.HTTP_CLIENT_HEADER
import static client.Resources.TEST_OIDS
import static client.Resources.TEST_TIME_INTERVAL
import static client.Resources.TEST_UUID

class ConfigurationDownloadServiceTest extends Specification {

    RestTemplate restTemplate = Mock()

    SecurityAuth securityAuth = Mock()

    Terminator terminator = Mock()

    ClientInstance clientInstance = new ClientInstance(TEST_UUID)

    @Subject
    ConfigurationDownloadService confDownloadService = new ConfigurationDownloadServiceImpl(
            restTemplate, securityAuth, clientInstance ,terminator
    )

    def "should return config from file"() {
        when: "configuration download method is performed on existing file"
        ConfigResponseDto configResponseDto = confDownloadService.getClientConfiguration(CONFIG_FILE_EXISTS)
        then: "returned time interval is the same as one within file"
        configResponseDto.timeInterval == TEST_TIME_INTERVAL
    }

    def "should download config from server when file does not exist"() {
        given: "server url to download config"
        ReflectionTestUtils.setField(confDownloadService, "url", "http://test:8080/configuration/config")
        when: "configuration download method is performed"
        ConfigResponseDto configResponseDto = confDownloadService.getClientConfiguration(CONFIG_FILE_NOT_EXISTS)
        then: "downloaded and saved config equals that inside file"
        configResponseDto.timeInterval == TEST_TIME_INTERVAL
        configResponseDto.nameOidPair == TEST_OIDS
        1 * securityAuth.createHeaders(TEST_UUID.toString(), TEST_UUID.toString()) >> HTTP_CLIENT_HEADER
        1 * restTemplate.exchange(
                _ as String, _ as HttpMethod, _ as HttpEntity, _ as Class) >> DOWNLOADED_CONFIG
    }

    def "should throw exception when server is down"() {
        given: "url to register on down server"
        ReflectionTestUtils.setField(confDownloadService, "url", "http://test:8080/configuration/config")
        when: "configuration download method is called"
        ConfigResponseDto configResponseDto = confDownloadService.getClientConfiguration(CONFIG_FILE_NOT_EXISTS)
        then: "exception is thrown"
        thrown(FailedToDownloadConfigException)
        1 * securityAuth.createHeaders(TEST_UUID.toString(), TEST_UUID.toString()) >> HTTP_CLIENT_HEADER
        1 * restTemplate.exchange(
                _ as String, _ as HttpMethod, _ as HttpEntity, _ as Class) >> { throw new RestClientException("error") }

    }

    def cleanup() {
        CONFIG_FILE_NOT_EXISTS.delete()
    }
}
