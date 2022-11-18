package client.config

import client.model.ClientInstance
import client.model.Config
import client.service.config.ConfigurationDownloadService
import client.service.registration.RegisterService
import client.tools.mappers.ClientMapper
import client.tools.mappers.ClientMapperImpl
import client.tools.mappers.ConfigMapper
import client.tools.mappers.ConfigMapperImpl
import client.tools.terminator.Terminator
import client.tools.terminator.TerminatorImpl
import org.springframework.context.ApplicationContext
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification
import spock.lang.Subject

import static client.Resources.MOCK_CLIENT_RESPONSE
import static client.Resources.MOCK_CONFIG_RESPONSE_DATA

class LaunchingConfigurationTest extends Specification {

    ConfigurationDownloadService configurationDownloadService = Mock()

    RegisterService registerService = Mock()

    ConfigMapper configMapper = new ConfigMapperImpl()

    ClientMapper clientMapper = new ClientMapperImpl()

    Terminator terminator = new TerminatorImpl(Mock(ApplicationContext))

    @Subject
    LaunchingConfiguration launchingConfiguration = new LaunchingConfiguration(
            configurationDownloadService, registerService, configMapper, clientMapper, terminator
    )
    
    def "should return config from file, when file exists"() {
        given: "path to configuration file"
        ReflectionTestUtils.setField(launchingConfiguration, "configFilePath", "filePath")
        when: "reading configuration and file exists"
        Config config = launchingConfiguration.readConfig()
        then:
        config != null
        1 * configurationDownloadService.getClientConfiguration(_ as File) >> MOCK_CONFIG_RESPONSE_DATA
    }

    def "should return clientInstance from file, when file exists"() {
        given: "path to registration file"
        ReflectionTestUtils.setField(launchingConfiguration, "registerFilePath", "filePath")
        when: "registering when file exists"
        ClientInstance clientInstance = launchingConfiguration.register()
        then:
        clientInstance != null
        1 * registerService.register(_ as File) >> MOCK_CLIENT_RESPONSE
    }
}
