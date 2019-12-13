package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dtos.InfluencerDto;
import web.dtos.PackDto;
import web.entities.Pack;
import web.entities.User;
import web.exceptions.WebApiReponse;
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
    public ResponseEntity<?> create(@CurrentUser UserPrincipal userPrincipal, @RequestBody PackDto packDto) {
        if(this.packService.findByName(packDto.getName()) != null) {
            return ResponseEntity.badRequest().body(new WebApiReponse(false, "pack_name_existed"));
        }
        Pack pack = new Pack();
        BeanUtils.copyProperties(packDto, pack);
        User user = new User();
        user.setId(userPrincipal.getId());
        pack.setUser(user);
        return ResponseEntity.ok(this.packService.create(pack));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody PackDto packDto) {
        if(this.packService.findByName(packDto.getName()) != null) {
            return ResponseEntity.badRequest().body(new WebApiReponse(false, "pack_name_existed"));
        }
        Pack pack = new Pack();
        BeanUtils.copyProperties(packDto, pack);
        return ResponseEntity.ok(this.packService.update(id, pack));
    }

    @PostMapping("{id}")
    public PackDetail removeAnInfluencer(@PathVariable("id") String id, @CurrentUser UserPrincipal userPrincipal, @RequestBody DeleteInfluencerRequest request) {

        if(request.isDeleteAll()) {
            List<Pack> packs = packService.findPackByInfluencer(request.getInfluencerId());
            List<Pack> newPacks = packs.stream().filter(p ->  p.getUser().getId().equals(userPrincipal.getId())).collect(Collectors.toList());
            for (Pack p:
                    newPacks) {
                if(!p.getId().equals(id)) {
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
