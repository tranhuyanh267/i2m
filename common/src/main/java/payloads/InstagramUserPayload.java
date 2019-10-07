package payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramUserPayload {
    private String userId;
    private String username;
    private List<String> categories;
    private String fullName;
    private String biography;
    private String followers;
    private String followings;
    private int mediaCount;
    private String profilePicUrl;
    private String email;
    private boolean isVerified;
    private String externalUrl;
    private List<PostPayload> posts;
}
