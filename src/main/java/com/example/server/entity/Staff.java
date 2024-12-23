package com.example.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Staff {

    @Id
    String staffId;
    String staffName;
    String staffEmail;
    String staffPass;
    @Enumerated(EnumType.STRING)
    StaffRole staffRole;

    public enum StaffRole{
        ROLE_ADMIN,
        ROLE_EMPLOYEE,
        ROLE_TL
    }

}
