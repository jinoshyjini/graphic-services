package com.designer.graphic.repository;

import com.designer.graphic.entities.VideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {
//}

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    // Add this method to find videos by category
    Page<VideoEntity> findByCategory(String category, Pageable pageable);
}



