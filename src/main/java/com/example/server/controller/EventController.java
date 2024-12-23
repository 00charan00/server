package com.example.server.controller;

import com.example.server.dto.EventDto;
import com.example.server.entity.Event;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("event")
@CrossOrigin
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("addEvent")
    public ResponseEntity<ResponseBase> addEvent(@RequestBody EventDto eventDto){
        ResponseBase response = new ResponseBase(eventService.saveNewEvent(eventDto),true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getEvent")
    public ResponseEntity<List<Event>> getEvent(){
        return ResponseEntity.ok(eventService.getAllEvent());
    }

    @PutMapping("cancel/{eventId}")
    public ResponseEntity<ResponseBase> cancelEvent(@PathVariable String eventId){
        return ResponseEntity.ok(eventService.cancelEvent(eventId));
    }
}