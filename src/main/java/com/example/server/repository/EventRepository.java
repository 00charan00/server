package com.example.server.repository;

import com.example.server.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface EventRepository extends JpaRepository<Event,String> {

    void deleteAllByEventDateBefore(Timestamp date);

}