package web.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteInfluencerRequest {
    private String influencerId;
    private boolean deleteAll;

}
