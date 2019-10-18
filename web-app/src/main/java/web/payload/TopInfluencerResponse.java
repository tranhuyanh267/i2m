package web.payload;

public interface TopInfluencerResponse {
     String getId();
     Integer getMail_count();
     String getUsername();
     String getFull_name();
     Integer getFollowers();
     Integer getFollowings();
     Integer getMedia_count();
     String getProfile_pic_url();
     Float getEngagement();
     String getBiography();
     String getEmail();

     String getExternal_url();
     Boolean getIs_verified();
}
