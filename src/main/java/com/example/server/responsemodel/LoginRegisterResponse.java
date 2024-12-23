package com.example.server.responsemodel;

import com.example.server.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRegisterResponse{
    String msg;
    boolean status;
    Staff.StaffRole role;
    String userName;
}
