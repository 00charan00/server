package com.example.server.repository;

import com.example.server.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,String> {
    List<Meeting> findAllByMeetingStatus(Meeting.MeetingStatus meetingStatus);

    void deleteMeetingByMeetingDateTimeBefore(Timestamp date);

}