package com.example.server.controller;

import com.example.server.dto.MeetingDto;
import com.example.server.entity.Meeting;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("meet")
@CrossOrigin
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @PostMapping("create")
    public ResponseEntity<ResponseBase> createMeeting(@RequestBody MeetingDto meetingDto){
        ResponseBase response = new ResponseBase(meetingService.createNewMeeting(meetingDto), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("active")
    public ResponseEntity<List<Meeting>> getActiveMeetings() {
        List<Meeting> activeMeetings = meetingService.getActiveMeetings();
        return ResponseEntity.ok(activeMeetings);
    }

    @GetMapping("all")
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("mine")
    public ResponseEntity<List<Meeting>> getMyMeetings(
            @RequestParam String staffId,
            @RequestParam String staffMail
    ) {
        List<Meeting> meetings = meetingService.getMyMeetings(staffId, staffMail);
        return ResponseEntity.ok(meetings);
    }

    @PutMapping("cancel/{meetId}")
    public ResponseEntity<ResponseBase> cancelMeet(@PathVariable String meetId){
        return ResponseEntity.ok(meetingService.cancelMeeting(meetId));
    }
}
