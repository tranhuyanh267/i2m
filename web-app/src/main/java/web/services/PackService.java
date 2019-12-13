package web.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dtos.InfluencerDto;
import web.dtos.PackDto;
import web.entities.Influencer;
import web.entities.Pack;
import web.payload.PackDetail;
import web.repositories.InfluencerRepository;
import web.repositories.PackRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PackService {
    @Autowired
    private PackRepository packRepository;
    @Autowired
    private InfluencerRepository influencerRepository;

    public List<Pack> getUserPacks(String userId) {
        return this.packRepository.findByUserId(userId).stream().map(item -> {
            item.setUser(null);
            item.getInfluencers().forEach(i -> {
                i.setPosts(null);
            });
            return item;
        }).collect(Collectors.toList());
    }

    public Pack getDetails(String id) {
        Pack pack = this.packRepository.findById(id).orElse(null);
//        if (pack != null) {
//            pack.setUser(null);
//        }
        return pack;
    }

    public PackDetail update(String id, Pack updateInfo) {
        Optional<Pack> packOpt = this.packRepository.findById(id);
        if (packOpt.isPresent()) {
            Pack pack = packOpt.get();
            pack.setName(updateInfo.getName());
            return transformPack(this.packRepository.save(pack));
        }
        return null;
    }

    public PackDetail removeAnInfluencer(String id, String influencerId) {
        Optional<Pack> packOpt = this.packRepository.findById(id);
        Optional<Influencer> influencerOpt = this.influencerRepository.findById(influencerId);

        if (packOpt.isPresent()) {
            Pack pack = packOpt.get();
            Influencer influencer = influencerOpt.get();
            if(pack.getInfluencers().remove(influencer)) {
                pack.setInfluencers(pack.getInfluencers());

                return transformPack(this.packRepository.save(pack));
            }

        }

        return null;
    }

    public PackDetail transformPack(Pack pack) {
        List<InfluencerDto> influencerDtos = new ArrayList<>();

        pack.getInfluencers().forEach(in -> {
            in.setPosts(null);
            List<Pack> packs = this.findPackByInfluencer(in.getId());
            List<Pack> newPacks = packs.stream().filter(p -> p.getUser().getId() == pack.getUser().getId()).collect(Collectors.toList());
            InfluencerDto influencerDto = new InfluencerDto();
            BeanUtils.copyProperties(in, influencerDto);
            influencerDto.setPacks(newPacks.stream().map(p -> new PackDto(p.getName())).collect(Collectors.toList()));
            influencerDtos.add(influencerDto);

        });

        PackDetail packDetail = new PackDetail(pack.getId(), pack.getName(), influencerDtos);
        return packDetail;
    }
    public Pack findById(String id) {
        return this.packRepository.findById(id).orElse(null);
    }

    public void delete(String id) {
        this.packRepository.deleteById(id);
    }

    public Pack create(Pack pack) {
        return this.packRepository.save(pack);
    }

    public List<Pack> findPackByInfluencer(String influencerId) {
        return this.packRepository.findByInfluencerId(influencerId);
    }

    public Pack findByName(String name) {
        return this.packRepository.findByName(name);
    }
}
