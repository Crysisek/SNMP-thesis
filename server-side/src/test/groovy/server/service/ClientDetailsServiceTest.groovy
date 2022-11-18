package server.service


import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification
import spock.lang.Subject

import static server.Resources.DEFAULT_ID
import static server.Resources.FAKE_ID
import static server.Resources.TEST_CLIENT
import static server.Resources.TEST_ID

class ClientDetailsServiceTest extends Specification {

    ClientService clientService = Mock()

    @Subject
    UserDetailsService clientDetailsService = new ClientDetailsService(clientService)

    def "should return client with given username"() {
        given: "default user ID"
        ReflectionTestUtils.setField(clientDetailsService, "defaultId", DEFAULT_ID.toString())
        when: "getting client by username"
        UserDetails clientDetails = clientDetailsService.loadUserByUsername(TEST_ID.toString())
        then: "getting specified user"
        noExceptionThrown()
        clientDetails.username == TEST_ID.toString()
        1 * clientService.findByUsername(_ as UUID) >> { UUID clientId ->
            TEST_CLIENT.username == clientId ? Optional.of(TEST_CLIENT) : Optional.empty()
        }
    }

    def "should throw exception when no client is found"() {
        given: "default user ID"
        ReflectionTestUtils.setField(clientDetailsService, "defaultId", DEFAULT_ID.toString())
        when: "getting client with wrong username"
        UserDetails clientDetails = clientDetailsService.loadUserByUsername(FAKE_ID.toString())
        then: "exception is thrown"
        thrown(UsernameNotFoundException)
        1 * clientService.findByUsername(_ as UUID) >> { UUID clientId ->
            TEST_CLIENT.username == clientId ? Optional.of(TEST_CLIENT) : Optional.empty()
        }
    }
}
