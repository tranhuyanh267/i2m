package crawler.utils;

import crawler.entities.Post;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;

public class Transform {
    public static Post transform(InstagramFeedItem item, String id, String type, String influencerId) {
        try {
            Post post = new Post();
            post.setId(id);
            post.setInfluencerId(influencerId);
            post.setCommentCount(item.getComment_count());
            post.setLikeCount(item.getLike_count());
            if (item.getCaption() != null) {
                post.setContent(item.getCaption().getText());
            }
            post.setCode(item.getCode());
            post.setViewCount(item.getView_count());
            post.setVideo(item.isHas_audio());
            post.setType(type);

            if (item.getImage_versions2() != null) {
                post.setThumbnailUrl(item.getImage_versions2().getCandidates().get(0).getUrl());
            } else {
                post.setThumbnailUrl(item.getCarousel_media().get(0).getImage_versions2().getCandidates().get(0).getUrl());
            }

            return post;
        } catch (Exception ex) {
            return null;
        }

    }
}
