package calculation.documents;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class Carousel {
    @Field("image_versions2.candidates")
    List<Candidate> candidates;
}
