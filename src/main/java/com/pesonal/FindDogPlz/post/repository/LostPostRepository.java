package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.domain.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostPostRepository extends JpaRepository<LostPost, Long> {
}
