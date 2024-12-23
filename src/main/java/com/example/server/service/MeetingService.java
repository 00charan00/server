package com.example.server.service;

import com.example.server.dto.MeetingDto;
import com.example.server.entity.Meeting;
import com.example.server.entity.Staff;
import com.example.server.repository.MeetingRepository;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeetingService {
    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    StaffService staffService;

    public String createNewMeeting(MeetingDto meetingDto){
        Meeting meeting= Mapper.meetingMapper(meetingDto);
        String meetingId=meetingRepository.save(meeting).getMeetingId();
        notifyMeetingAll(meeting);
        return meetingId;
    }


    public void notifyMeetingAll(Meeting meeting){
        log.info("notifying users on meeting started");
        List<Staff> memberStaffs = staffService.getStaffsByIds(meeting.getEmpIdList());
        List<String> staffNameList = memberStaffs.stream().map(st->st.getStaffName())
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("Meeting Details:\n")
                .append("-------------------\n")
                .append("Meeting ID: ").append(meeting.getMeetingId()).append("\n")
                .append("Meeting Name: ").append(meeting.getMeetingName()).append("\n")
                .append("Description: ").append(meeting.getMeetingDescription()).append("\n")
                .append("Date and Time: ").append(meeting.getMeetingDateTime()).append("\n")
                .append("Meeting Link: ").append(meeting.getMeetingLink()).append("\n")
                .append("Owner: ").append(meeting.getMeetingOwner()).append("\n")
                .append("Participants: ").append(String.join(", ", staffNameList)).append("\n")
                .append("\nPlease make sure to attend on time.\n");
        staffService.sendNotificationsToStaffs(meeting.getEmpIdList(), sb.toString(), meeting.getMeetingName());
        log.info("notifying users on meeting ended");
    }
    @Transactional
    public void deleteExpiredMeetings(){
        meetingRepository.deleteMeetingByMeetingDateTimeBefore(Timestamp.from(Instant.now()));
    }

    public ResponseBase cancelMeeting(String meetingId){
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new RuntimeException(""));
        meeting.setMeetingStatus(Meeting.MeetingStatus.CANCELLED);
        meetingRepository.save(meeting);
        return new ResponseBase("Meeting cancelled",true);
    }
    public List<Meeting> getActiveMeetings() {
        return meetingRepository.findAllByMeetingStatus(Meeting.MeetingStatus.ACTIVE);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public List<Meeting> getMyMeetings(String staffId,String staffEmail){
        return meetingRepository.findAll().stream().filter(
                        m -> m.getEmpIdList().contains(staffId) ||
                                m.getMeetingOwner().equals(staffEmail)
                )
                .toList();
    }
}
