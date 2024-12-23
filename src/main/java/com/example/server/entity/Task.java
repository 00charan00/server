package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id
    String taskId;
    String taskTittle;
    String taskDescription;
    @OneToOne
    Team team;
    @ElementCollection
    Map<String,ProgressStatus> moduleLevelProgress = new TreeMap<>();
    @Enumerated(EnumType.STRING)
    ProgressStatus taskProgress;
    double progressPercent;
    boolean teamAssigned;
    @OneToMany(fetch = FetchType.EAGER)
    Set<ProgressInfo> progressDetails;
    @ElementCollection
    List<String> modules;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm a")
    Date deadline;

    public enum ProgressStatus{
        INITIATED,
        IN_PROGRESS,
        DONE,
        IN_REVIEW,
        APPROVED,
        REJECTED
    }
}
