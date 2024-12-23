package com.example.server.service;

import com.example.server.dto.EventDto;
import com.example.server.entity.Event;
import com.example.server.repository.EventRepository;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.utils.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public String saveNewEvent(EventDto eventDto){
        Event event= Mapper.eventMapper(eventDto);
        return eventRepository.save(event).getEventId();
    }

    public List<Event> getAllEvent(){
        return eventRepository.findAll();
    }

    public ResponseBase cancelEvent(String eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("no such event"));
        event.setEventStatus(Event.EventStatus.CANCELLED);
        eventRepository.save(event);
        return new ResponseBase("Event Cancelled",true);
    }
    @Transactional
    public void deleteExpiredEvents() {
        eventRepository.deleteAllByEventDateBefore(Timestamp.from(Instant.now()));
    }
}
