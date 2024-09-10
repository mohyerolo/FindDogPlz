package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FindPostRepository extends JpaRepository<FindPost, Long> {
    @Query(value = "SELECT ST_DISTANCE_SPHERE(POINT(:lon, :lat), fp.loc_point) AS dist FROM find_post fp WHERE fp.id = :id", nativeQuery = true)
    Double findDistByPoint(@Param("lon") Double lon, @Param("lat") Double lat, @Param("id") Long id);
}
