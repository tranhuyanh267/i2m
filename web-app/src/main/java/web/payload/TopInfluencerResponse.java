package web.payload;

import java.util.Date;

public interface TopInfluencerResponse {
     String getId();
     Integer getMail_count();
     String getUsername();
     String getFull_name();
     Integer getFollowers();
     Integer getFollowings();
     String getProfile_pic_url();
     Float getEngagement();
     String getEmail();
     Float getAverage_comment_per_post();
     Float getAverage_like_per_post();
     Float getWeight();
}
