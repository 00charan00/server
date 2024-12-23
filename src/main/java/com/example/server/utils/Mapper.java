package com.example.server.utils;

import com.example.server.dto.EventDto;
import com.example.server.dto.MeetingDto;
import com.example.server.dto.StaffDto;
import com.example.server.entity.Event;
import com.example.server.entity.Meeting;
import com.example.server.entity.Staff;

import java.util.UUID;

public class Mapper {

    public static Staff staffMapper(StaffDto staffDto){
        return new Staff(
                "Staff:"+ UUID.randomUUID(),
                staffDto.getStaffName(),
                staffDto.getStaffEmail(),
                staffDto.getStaffPass(),
                Staff.StaffRole.ROLE_EMPLOYEE
        );
    }

    public static Event eventMapper(EventDto eventDto ){
        return new Event(
                "Event:"+ UUID.randomUUID(),
                eventDto.getEventName(),
                eventDto.getEventVenue(),
                eventDto.getEventDate(),
                eventDto.getEventDescription(),
                Event.EventStatus.ACTIVE,
                eventDto.getEventPoster()
        );
    }

    public static Meeting meetingMapper(MeetingDto meetingDto){
        return new Meeting(
                "Meeting:"+UUID.randomUUID(),
                meetingDto.getMeetingName(),
                meetingDto.getMeetingDescription(),
                meetingDto.getEmpIdList(),
                meetingDto.getMeetingDateTime(),
                meetingDto.getMeetingOwner(),
                Meeting.MeetingStatus.ACTIVE,
                meetingDto.getMeetingLink()
        );
    }
}