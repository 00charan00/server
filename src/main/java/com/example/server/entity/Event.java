package com.example.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Event {
    @Id
    String eventId;
    String eventName;
    String eventVenue;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm a")
    private Timestamp eventDate;
    String eventDescription;
    @Enumerated(EnumType.STRING)
    EventStatus eventStatus;
    String eventPoster;


    public enum EventStatus{
        ACTIVE,
        CANCELLED,
        COMPLETED
    }
}
