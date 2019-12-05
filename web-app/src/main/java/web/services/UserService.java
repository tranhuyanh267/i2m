package web.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.entities.User;
import web.exceptions.WebAppException;
import web.model.UpdatePasswordModel;
import web.model.UserUpdateModel;
import web.repositories.UserRepository;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private GoogleCloudService googleCloudService;



    public User getUserDetails(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new WebAppException("User id not found " + id));
    }

    public boolean updateAvatar(MultipartFile file, String userId) {

        try {
            String imageUrl = googleCloudService.uploadFile(file, userId);
            this.userRepository.updateImageUrl(userId, imageUrl);

        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public User updateUser(UserUpdateModel newUser,String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WebAppException("Not found user."));
        if (newUser.getFullName() != null || newUser.getFullName().length() > 0) {
            user.setFullName(newUser.getFullName());
        }
        user.setCategories(newUser.getCategories());

       return userRepository.save(user);
    }

    public boolean updatePassword(String userId, UpdatePasswordModel model){
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new WebAppException("Not found user"));

        if (!passwordEncoder.matches(model.getOldPassword(), u.getPassword())) {
            return false;
        }

        u.setPassword(passwordEncoder.encode(model.getPassword()));
        userRepository.save(u);
        return true;
    }
}
