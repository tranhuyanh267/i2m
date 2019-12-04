package web.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.entities.Influencer;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfluencerDto extends Influencer {
   private List<PackDto> packs;
}
