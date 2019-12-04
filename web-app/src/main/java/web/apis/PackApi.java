package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import web.dtos.InfluencerDto;
import web.dtos.PackDto;
import web.entities.Pack;
import web.entities.User;
import web.payload.DeleteInfluencerRequest;
import web.payload.PackDetail;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.PackService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/packs")
@AllArgsConstructor
public class PackApi {
    private PackService packService;

    @GetMapping("{id}")
    public PackDetail getDetails(@PathVariable("id") String id) {
        Pack details = this.packService.getDetails(id);


        return this.packService.transformPack(details);
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
    public PackDetail update(@PathVariable("id") String id, @RequestBody PackDto packDto) {
        Pack pack = new Pack();
        BeanUtils.copyProperties(packDto, pack);
        return this.packService.update(id, pack);
    }

    @PostMapping("{id}")
    public PackDetail removeAnInfluencer(@PathVariable("id") String id, @RequestBody DeleteInfluencerRequest request) {
        if(request.isDeleteAll()) {
            List<Pack> packs = packService.findPackByInfluencer(request.getInfluencerId());
            for (Pack p:
                 packs) {
                if(p.getId() != id) {
                    this.packService.removeAnInfluencer(p.getId(), request.getInfluencerId());
                }
            }
        }
        return this.packService.removeAnInfluencer(id, request.getInfluencerId());
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        this.packService.delete(id);
    }

}
