package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Meeting {
    @Id
    String meetingId;
    String meetingName;
    String meetingDescription;
    @ElementCollection
    List<String> empIdList;
    Timestamp meetingDateTime;
    String meetingOwner;
    @Enumerated(EnumType.STRING)
    MeetingStatus meetingStatus;
    String meetingLink;

    public enum MeetingStatus{
        ACTIVE,
        CANCELLED
    }
}
