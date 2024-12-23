package com.example.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Team {
    @Id
    String teamId;
    String teamName;
    @ManyToMany
    List<Staff> teamMembers;
    @ManyToOne
    Staff teamLeader;
}
