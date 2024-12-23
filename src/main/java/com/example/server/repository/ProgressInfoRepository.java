package com.example.server.repository;

import com.example.server.entity.ProgressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressInfoRepository extends JpaRepository<ProgressInfo,String> {
}
