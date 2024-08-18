package com.pesonal.FindDogPlz.timeLine.repository;

import com.pesonal.FindDogPlz.timeLine.domain.TimeLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeLineRepository extends JpaRepository<TimeLine, Long> {

    boolean existsByReportId(Long reportId);
    List<TimeLine> findByLostPostIdOrderByReportedDateDesc(Long lostPostId);
}
