package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.constants.RoleName;
import web.entities.User;
import web.exceptions.WebApiReponse;
import web.exceptions.WebAppException;
import web.payload.LoginRequest;
import web.payload.SignUpRequest;
import web.repositories.CategoryRepository;
import web.repositories.UserRepository;
import web.security.JwtTokenProvider;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationApi {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        User u = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new WebAppException("Not found user"));
        if(!u.isActive()){
            return ResponseEntity.badRequest().body(new WebAppException("User is locked"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new WebApiReponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("existed_email");
        }

        // Creating user's account
        User user = new User();
        user.setFullName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setRole(RoleName.USER_ROLE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setCategories(signUpRequest.getCategory());
        user.setActive(true);
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/self")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new WebApiReponse(true, "User registered successfully@"));
    }

}
