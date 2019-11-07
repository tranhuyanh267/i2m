package web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import web.constants.AppConstants;
import web.entities.User;
import web.payload.PagedResponse;
import web.services.AdminService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
public class AdminApi {

    @Autowired
    AdminService adminService;

    @PostMapping("")
    //@PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<User> getAllUser(@RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        Page<User> user = adminService.getAllUser(page,size);

        List<User> listUser = user.getContent().stream().map(item -> {
            item.setPassword(null);
            item.getCategories().size();
            return item;
        }).collect(Collectors.toList());

        return new PagedResponse<>(listUser,user.getNumber(), user.getSize(), user.getTotalElements(), user.getTotalPages(), user.isLast());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateIsActiveUser(@PathVariable("userId") String userId){
        User u = adminService.updateIsActivedUser(userId);
        return ResponseEntity.ok("User is activeted");
    }
}
