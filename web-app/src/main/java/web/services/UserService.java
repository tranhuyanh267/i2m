package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.User;
import web.exceptions.WebAppException;
import web.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User getUserDetails(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new WebAppException("User id not found " + id));
    }

}
