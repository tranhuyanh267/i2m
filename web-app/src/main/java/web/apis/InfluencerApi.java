package web.apis;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import web.constants.AppConstants;
import web.entities.Influencer;
import web.entities.User;
import web.exceptions.WebApiReponse;
import web.payload.InfluencerMyListRequest;
import web.payload.PagedResponse;
import web.payload.TopInfluencerResponse;
import web.repositories.InfluencerRepository;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.InfluencerService;
import web.services.UserService;
import web.util.InfluencerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/influencers")
public class InfluencerApi {

    private InfluencerRepository influencerRepository;
    private InfluencerService influencerService;
    private UserService userService;

    @GetMapping
    public PagedResponse<Influencer> getInfluencers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                    @RequestParam(value = "sortBy", defaultValue = "followers") String sortBy,
                                                    @RequestParam(value = "search", defaultValue = "") String search,
                                                    @Nullable @RequestParam(value = "minFollowers") Integer minFollowers,
                                                    @Nullable @RequestParam(value = "maxFollowers") Integer maxFollowers,
                                                    @Nullable @RequestParam(value = "minEngagement") Float minEngagement,
                                                    @Nullable @RequestParam(value = "maxEngagement") Float maxEngagement,
                                                    @Nullable @RequestParam(value = "categories[]") String[] categories
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortBy);

        if (maxFollowers == null) {
            Page<Influencer> infulenerLists = influencerRepository.findByUsernameAndFullName(search, pageable);
            return normalizeResponse(infulenerLists);
        }

            if(categories == null) {
                Page<Influencer> infulenerLists = influencerRepository.filterInfluencer(search, minFollowers, maxFollowers, minEngagement, maxEngagement, pageable);
                return normalizeResponse(infulenerLists);
            }

            Page<Influencer> infulenerLists = influencerRepository.filterInfluencerHasCategories(minFollowers, maxFollowers, minEngagement, maxEngagement, categories, search, pageable);
            return normalizeResponse(infulenerLists);

    }

    @GetMapping("/{id}")
    public Influencer getInfluencerDetail(@PathVariable(value = "id") String influencerId) {

        return influencerService.getInfluencerDetail(influencerId);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addToPack(@PathVariable(value = "id") String influencerId, @RequestBody InfluencerMyListRequest influencerMyListRequest) {
        String result = this.influencerService.addInfluencerToPack(influencerId, influencerMyListRequest.getPackId());
        if(result != "") {
            return ResponseEntity.badRequest().body(new WebApiReponse(false, result));
        }
        return ResponseEntity.ok(new WebApiReponse(true, result));
    }


    @GetMapping("/suggestion")
    public List<Influencer> getTopInfluencer(@CurrentUser UserPrincipal userPrincipal) {
        if(userPrincipal != null) {
            User userDetails = userService.getUserDetails(userPrincipal.getId());


            if(userDetails.getCategories().size() > 0) {
                List<String> categoryIds = userDetails.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList());
                return influencerRepository.findByUserCategory(categoryIds);
            }
        }

        return influencerRepository.findOrderByFollowersDescLimitTo(9);

    }

    @GetMapping("/load-more")
    public List<Influencer> fillInToHomePage(@RequestParam(value = "limit") int limit) {
        return influencerRepository.findOrderByFollowersDescLimitTo(limit);
    }

    private PagedResponse<Influencer> normalizeResponse(Page<Influencer> infulenerLists) {
        List<Influencer> result = infulenerLists.getContent().stream().map(item -> {
            item.getCategories().size();
            item.setPosts(null);
            return item;
        }).collect(Collectors.toList());

        return new PagedResponse<>(result, infulenerLists.getNumber(), infulenerLists.getSize(), infulenerLists.getTotalElements(), infulenerLists.getTotalPages(), infulenerLists.isLast());
    }

    @GetMapping("/ranking")
    public List<TopInfluencerResponse> findTopInfluencers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        List<TopInfluencerResponse> influencerResponses = influencerService.findTopInfluencer(page, size);

        return influencerResponses;
    }

    @GetMapping("/prediction")
    public float[] predictInfluencerProfile() {
        try {
            String modelWeights = new ClassPathResource("model_weights.h5").getFile().getPath();
            String modelJson = new ClassPathResource("model_config.json").getFile().getPath();
            MultiLayerNetwork network = KerasModelImport.importKerasSequentialModelAndWeights(modelJson, modelWeights);

            List<Influencer> influencers = influencerRepository.findByAuthentic();
            float[][] data = new float[influencers.size()][15];
            for (int i = 0; i < influencers.size() - 1; i++) {
                Influencer influencer = influencers.get(i);
                float[] features = new float[15];
                features[0] = InfluencerUtils.encodeUsername(influencer.getUsername().toLowerCase());
                features[1] = influencer.getUsername().split("_|\\.| ").length;
                features[2] = influencer.getFullName().length();
                features[3] = influencer.getFullName().split("_|\\.| ").length;
                features[4] = influencer.getFullName().chars().filter(c -> c == '?').count();
                if(influencer.getBiography() == null) {
                    influencer.setBiography("");
                }
                features[5] = influencer.getBiography().split("_|\\.| ").length;
                features[6] = influencer.getBiography().chars().filter(c -> c == '?').count();
                if(influencer.getFollowings() > 0) {
                    features[7] = (float) (influencer.getFollowers() / (influencer.getFollowings() * 1.0));
                } else {
                    features[7] = 0;
                }
                features[8] = influencer.getFollowers();
                features[9] = influencer.getFollowings();
                features[10] = influencer.getMediaCount();
                features[11] = influencer.getUserTagCount();
                features[12] = influencer.isPrivate() ? 1 : 0;
                features[13] = influencer.isVerified() ? 1 : 0;
                features[14] = influencer.isHasAnonymousProfilePicture() ? 1 : 0;
                data[i] = features;
            }




            INDArray abc = Nd4j.create(data);
            INDArray result = network.output(abc);
            DataBuffer dataBuffer = result.data();
            List<Influencer> newInfluencers = new ArrayList<>();

           float[] intRes = dataBuffer.asFloat();
            for (int i = 0; i < intRes.length - 1; i++) {
                if(intRes[i] >= 0.8757) {
                    newInfluencers.add(influencers.get(i));
                }
            }

            for (Influencer i:
                 newInfluencers) {
                i.setAuthentic(true);
                influencerRepository.save(i);
            }
            return intRes;
        } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/toJson")
    public void generateToJson() throws IOException {

        JSONObject arrayData = new JSONObject();
        List<Influencer> result = influencerRepository.filterInfluencer();
        for (Influencer in:
             result) {
            JSONObject sampleObject = new JSONObject();
            sampleObject.put("is_authentic", in.isAuthentic());
            sampleObject.put("has_anonymous_profile_picture", in.isHasAnonymousProfilePicture());
            sampleObject.put("following_count", in.getFollowings());
            sampleObject.put("follower_count", in.getFollowers());
            sampleObject.put("username", in.getUsername());
            sampleObject.put("full_name", in.getFullName());
            if(in.getBiography() == null) {
                sampleObject.put("biography", "");
            }
            else {
                sampleObject.put("biography", in.getBiography());
            }

            sampleObject.put("media_count", in.getMediaCount());
            sampleObject.put("usertags_count", in.getUserTagCount());
            sampleObject.put("has_anonymous_profile_picture", in.isHasAnonymousProfilePicture());
            sampleObject.put("is_private", in.isPrivate());
            sampleObject.put("is_verified", in.isVerified());

            arrayData.put(in.getId(), sampleObject);
        }

        Files.write(Paths.get("classified_profiles.json"), arrayData.toJSONString().getBytes());
    }


}
