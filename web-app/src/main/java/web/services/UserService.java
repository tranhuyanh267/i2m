package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.entities.User;
import web.exceptions.WebAppException;
import web.repositories.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User getUserDetails(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new WebAppException("User id not found " + id));
    }

    public boolean updateAvatar(MultipartFile file, String userId) {
        String fileNameExtension = (file.getOriginalFilename().split("\\.")[file.getOriginalFilename().split("\\.").length - 1]).toLowerCase();
        String fileName = userId + "." + fileNameExtension;
        try {
            this.userRepository.updateImageUrl(userId, fileName);
            fileStore(file, userId + "");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static File fileStore(MultipartFile file, String filename) throws IOException {
        //File Rename
        String[] extensions = file.getOriginalFilename().split("\\.");
        filename = filename + "." + extensions[extensions.length - 1];
        File result = new File("Media/User/" + filename);
        //File Store
        result.createNewFile();
        FileOutputStream fos = new FileOutputStream(result);
        fos.write(file.getBytes());
        fos.close();
        return result;
    }
}
