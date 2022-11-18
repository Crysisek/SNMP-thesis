package client.service.security


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import spock.lang.Specification

import static client.Resources.HTTP_ADMIN_HEADER
import static client.Resources.HTTP_CLIENT_HEADER

@SpringBootTest(classes = SecurityAuthImpl.class)
class SecurityAuthTest extends Specification {

    @Autowired
    SecurityAuth securityAuth

    def "should return header corresponding to provided arguments"() {
        when:
        HttpHeaders headers = securityAuth.createHeaders(user, pass)
        then:
        headers == result
        where:
        user     | pass     || result
        "client" | "client" || HTTP_CLIENT_HEADER
        "admin"  | "admin"  || HTTP_ADMIN_HEADER
    }
}
