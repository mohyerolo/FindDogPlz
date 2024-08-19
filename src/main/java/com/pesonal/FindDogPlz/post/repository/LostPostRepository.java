package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.domain.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LostPostRepository extends JpaRepository<LostPost, Long> {
    @Query(value = "SELECT ST_DISTANCE_SPHERE(POINT(:lon, :lat), lp.lost_point) AS dist FROM lost_post lp WHERE lp.id = :id", nativeQuery = true)
    Double findDistByPoint(@Param("lon") Double lon, @Param("lat") Double lat, @Param("id") Long id);
}
