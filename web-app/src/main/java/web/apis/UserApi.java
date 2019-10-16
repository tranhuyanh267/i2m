package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.entities.Pack;
import web.entities.User;
import web.exceptions.WebApiReponse;
import web.model.UpdatePasswordModel;
import web.model.UserUpdateModel;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.PackService;
import web.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserApi {

    @Autowired
    private UserService userService;
    @Autowired
    private PackService packService;

    @GetMapping("/self")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User userDetails = userService.getUserDetails(userPrincipal.getId());
        if(userDetails.getPassword() != null){
            userDetails.setPassword("1");
        }
        System.out.println(userDetails);

        return userDetails;
    }

    @GetMapping("/self/packs")
    public List<Pack> getMyPacks(@CurrentUser UserPrincipal userPrincipal) {
        return this.packService.getUserPacks(userPrincipal.getId());
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> avatarUpdate(@RequestParam("file") MultipartFile img, @CurrentUser UserPrincipal userPrincipal) {
        if (img != null) {
            try {
                //Get File Name Extension
                String fileNameExtension = (img.getOriginalFilename().split("\\.")[img.getOriginalFilename().split("\\.").length - 1]).toLowerCase();
                if (!fileNameExtension.equals("png") && !fileNameExtension.equals("jpg") && !fileNameExtension.equals("jpeg") && !fileNameExtension.equals("gif")) {
                    return ResponseEntity.badRequest().body("Invalid Image!!!");
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid Image");
            }
        }
        String userId;
        try {
            userId = userPrincipal.getId();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid user!!!");
        }
        return userService.updateAvatar(img, userId) ? ResponseEntity.ok("Update Successfully!!!")
                : ResponseEntity.badRequest().body("Update Fail!!!");
    }

    @PutMapping("/{userId}/update")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUser(
            @PathVariable(value = "userId") String userId,
            @Valid @RequestBody UserUpdateModel newUser
    ) {
        if (userService.updateUser(newUser, userId) == null)
            return ResponseEntity.badRequest().body(new WebApiReponse(false, "Update user failed."));
        return ResponseEntity.ok(new WebApiReponse(true, "Update user success"));
    }

    @PutMapping("/{userId}/update-password")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePassword(
            @PathVariable("userId") String userId,
            @Valid @RequestBody UpdatePasswordModel model
    ) {
        if (!userService.updatePassword(userId, model)) {
            return new ResponseEntity<String>("Old Password not matches.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new WebApiReponse(true, "Update password success"));
    }
}
