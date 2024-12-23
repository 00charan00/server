package com.example.server.repository;

import com.example.server.entity.ImageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDetailRepository extends JpaRepository<ImageDetail,String> {
}
