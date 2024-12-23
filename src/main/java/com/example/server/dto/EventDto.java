package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    String eventName;
    String eventVenue;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm a")
    private Timestamp eventDate;
    String eventDescription;
    String eventPoster;
}