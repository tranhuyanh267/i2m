package web.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.entities.User;
import web.exceptions.WebAppException;
import web.repositories.UserRepository;

@Service
@AllArgsConstructor
public class AdminService {
    @Autowired
    UserRepository userRepository;

    public Page<User> getAllUser(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return userRepository.findAllUser(pageable);
    }

    public User updateIsActivedUser(String userId){
        User u = userRepository.findById(userId).orElseThrow(()-> new WebAppException("Not found user."));
        u.setActive(!u.isActive());
        return userRepository.save(u);
    }
}
