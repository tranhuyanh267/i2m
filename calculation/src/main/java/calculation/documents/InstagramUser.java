package calculation.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramUser {
    @Id
    private String id;
    @Field("full_name")
    private String fullName;
    private List<String> categories = new ArrayList<>();
    @Field("follower_count")
    private int followers;
    @Field("usertags_count")
    private int userTagCount;
    @Field("following_count")
    private int followings;
    @Field("media_count")
    private int mediaCount;
    private String biography;
    @Field("public_phone_number")
    public String publicPhoneNumber;
    @Field("public_email")
    private String email;
    @Field("external_url")
    private String externalUrl;
    @Field("is_verify")
    private boolean isVerified;
    @Field("username")
    private String username;
    @Field("profile_pic_url")
    private String profilePicUrl;
    @Field("has_anonymous_profile_picture")
    private boolean hasAnonymousProfilePicture;
    @Field("is_private")
    private boolean isPrivate;
}
