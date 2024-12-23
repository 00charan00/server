package com.example.server.service;

import com.example.server.dto.StaffUpdateDto;
import com.example.server.dto.TeamDto;
import com.example.server.entity.Staff;
import com.example.server.entity.Team;
import com.example.server.exception.DataNotFoundException;
import com.example.server.exception.InvalidDataException;
import com.example.server.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    StaffService staffService;

    public String createTeam(TeamDto teamDto) {
        return formTeam(teamDto).getTeamId();
    }

    public String deleteTeam(String teamId) {
        teamRepository.deleteById(teamId);
        return "Team Deleted";
    }

    public Team formTeam(TeamDto teamDto) {
        List<Staff> members = staffService.getStaffsByIds(teamDto.getTeamMembers());
        Staff leader = staffService.getStaffById(teamDto.getTeamLeader());
        if(members != null && leader != null){
            StaffUpdateDto staffUpdateDto = new StaffUpdateDto();
            staffUpdateDto.setStaffRole(Staff.StaffRole.ROLE_TL.toString());
            staffService.updateStaff(teamDto.getTeamLeader(),staffUpdateDto);
            return teamRepository.save(new Team("Team"+ UUID.randomUUID(),teamDto.getTeamName(),members,leader));
        }else throw new InvalidDataException("Please provide team member and leader details");
    }

    public Team getTeamById(String teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new DataNotFoundException("No such Team found with id : "+teamId));
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getMyTeams(String staffEmail) {
        return teamRepository.findAll()
                .stream().filter(team ->
                        team.getTeamLeader().getStaffEmail().equals(staffEmail) ||
                                team.getTeamMembers().stream().map(t -> t.getStaffEmail()).toList().contains(staffEmail)

                ).collect(Collectors.toList());
    }
}
