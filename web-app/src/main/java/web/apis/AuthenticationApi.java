package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import web.services.AuthenticationService;

@RestController
@AllArgsConstructor
public class AuthenticationApi {
    private AuthenticationService authenticationService;

}
