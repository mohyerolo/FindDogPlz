package com.pesonal.FindDogPlz.report.repository;

import com.pesonal.FindDogPlz.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}