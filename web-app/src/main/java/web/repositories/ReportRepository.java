package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.entities.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    @Query(value = "SELECT * FROM i2m.report where influencer_id = ?1 and created_date >= subdate(NOW(), INTERVAL 1 MONTH) order by created_date", nativeQuery = true)
    List<Report> findReportByInfluencer(String influencerId);
}
