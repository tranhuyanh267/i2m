package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.entities.Report;
import web.repositories.ReportRepository;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/report")
public class ReportApi {

    private ReportRepository reportRepository;

    @GetMapping("/{id}")
    public List<Report> fetchFollowersChart(@PathVariable(value = "id") String influencerId) {
        return reportRepository.findByInfluencerIdOrderByCreatedDate(influencerId);
    }
}
