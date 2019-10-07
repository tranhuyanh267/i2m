package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import web.dtos.PackDto;
import web.entities.Pack;
import web.entities.User;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.PackService;

@RestController
@RequestMapping("/api/packs")
@AllArgsConstructor
public class PackApi {
    private PackService packService;

    @GetMapping("{id}")
    public Pack getDetails(@PathVariable("id") String id) {
        Pack details = this.packService.getDetails(id);
        details.getInfluencers().forEach(in -> {
            in.setPosts(null);
            in.setPacks(null);
        });
        return details;
    }

    @PostMapping
    public Pack create(@CurrentUser UserPrincipal userPrincipal, @RequestBody PackDto packDto) {
        Pack pack = new Pack();
        BeanUtils.copyProperties(packDto, pack);
        User user = new User();
        user.setId(userPrincipal.getId());
        pack.setUser(user);
        return this.packService.create(pack);
    }

    @PutMapping("{id}")
    public Pack update(@PathVariable("id") String id, @RequestBody PackDto packDto) {
        Pack pack = new Pack();
        BeanUtils.copyProperties(packDto, pack);
        return this.packService.update(id, pack);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        this.packService.delete(id);
    }
}
