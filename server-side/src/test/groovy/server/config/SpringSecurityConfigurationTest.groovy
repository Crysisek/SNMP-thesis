package server.config


import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.util.ReflectionTestUtils
import server.model.Client
import server.service.ClientDetailsService
import server.service.ClientService
import spock.lang.Specification
import spock.lang.Subject

import static server.Resources.ADMIN_CLIENT
import static server.Resources.ADMIN_ID
import static server.Resources.DEFAULT_CLIENT
import static server.Resources.DEFAULT_ID

class SpringSecurityConfigurationTest extends Specification {

    ClientService clientService = Mock()

    ClientDetailsService clientDetailsService = Mock()

    PasswordEncoder passwordEncoder = Mock()

    @Subject
    SpringSecurityConfiguration securityConfiguration = new SpringSecurityConfiguration(
            clientService,
            passwordEncoder,
            clientDetailsService
    )

    def "should return authentication provider"() {
        when:
        def authProvider = securityConfiguration.authenticationProvider()
        then:
        authProvider != null
    }

    def "should create default and admin clients and save them to database"() {
        given: "default ID and admin ID"
        ReflectionTestUtils.setField(securityConfiguration, "defaultId", DEFAULT_ID.toString())
        ReflectionTestUtils.setField(securityConfiguration, "adminId", ADMIN_ID.toString())
        when: "application starts"
        securityConfiguration.createDefaultClient()
        then: "default and admin clients are created and saved"
        2 * clientService.existsById(_ as UUID) >> { UUID clientId ->
            clientId == DEFAULT_ID || clientId == ADMIN_ID
        }
        2 * clientService.save(_ as Client) >> { Client client ->
            client.username == DEFAULT_CLIENT.username ? DEFAULT_CLIENT : ADMIN_CLIENT
        }
    }
}
