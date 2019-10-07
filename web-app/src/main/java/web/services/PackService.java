package web.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.entities.Pack;
import web.repositories.PackRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PackService {
    private PackRepository packRepository;

    public List<Pack> getUserPacks(String userId) {
        return this.packRepository.findByUserId(userId).stream().map(item -> {
            item.setUser(null);
            item.getInfluencers().forEach(i -> {
                i.setPosts(null);
                i.setPacks(null);
            });
            return item;
        }).collect(Collectors.toList());
    }

    public Pack getDetails(String id) {
        Pack pack = this.packRepository.findById(id).orElse(null);
        if (pack != null) {
            pack.setUser(null);
        }
        return pack;
    }

    public Pack update(String id, Pack updateInfo) {
        Optional<Pack> packOpt = this.packRepository.findById(id);
        if (packOpt.isPresent()) {
            Pack pack = packOpt.get();
            pack.setName(updateInfo.getName());
            return this.packRepository.save(pack);
        }
        return null;
    }

    public void delete(String id) {
        this.packRepository.deleteById(id);
    }

    public Pack create(Pack pack) {
        return this.packRepository.save(pack);
    }
}
