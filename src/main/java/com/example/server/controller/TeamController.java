package com.example.server.controller;

import com.example.server.dto.TeamDto;
import com.example.server.entity.Team;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("team")
public class TeamController {

    @Autowired
    TeamService teamService;

    @PostMapping("create")
    public ResponseEntity<ResponseBase> createTeam(@RequestBody TeamDto teamDto){
        ResponseBase response = new ResponseBase(teamService.createTeam(teamDto),true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{teamId}")
    public ResponseEntity<ResponseBase> deleteTeam(@PathVariable String teamId){
        ResponseBase response = new ResponseBase(teamService.deleteTeam(teamId),true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("all")
    public ResponseEntity<List<Team>> getAllTeams(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("mine")
    public ResponseEntity<List<Team>> getMyTeams(@RequestParam String staffEmail){
        return ResponseEntity.ok(teamService.getMyTeams(staffEmail));
    }


    @GetMapping("tm/{teamId}")
    public ResponseEntity<Team> getTeam(String teamId){
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

}
