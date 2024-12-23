package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    String taskTittle;
    String taskDescription;
    List<String> modules;
    List<String> teamMembers;
    String teamLeader;
    String teamName;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm a")
    Date deadline;
}
