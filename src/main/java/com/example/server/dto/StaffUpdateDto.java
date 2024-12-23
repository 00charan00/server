package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffUpdateDto {
    String staffName;
    String staffEmail;
    String staffPass;
    String staffRole;
}