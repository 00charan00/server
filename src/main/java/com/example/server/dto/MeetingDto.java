package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetingDto {
    String meetingName;
    String meetingDescription;
    List<String> empIdList;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm a")
    Timestamp meetingDateTime;
    String meetingOwner;
    String meetingLink;
}