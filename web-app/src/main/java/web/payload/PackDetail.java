package web.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.dtos.InfluencerDto;
import web.entities.Pack;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackDetail extends Pack {
    private String id;
    private String name;
    private List<InfluencerDto> influencerList;


}
