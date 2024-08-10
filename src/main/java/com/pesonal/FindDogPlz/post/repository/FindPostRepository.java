package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FindPostRepository extends JpaRepository<FindPost, Long> {
}
