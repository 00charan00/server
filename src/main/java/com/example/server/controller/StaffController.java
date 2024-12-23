package com.example.server.controller;

import com.example.server.dto.AdminStaffDto;
import com.example.server.dto.StaffBase;
import com.example.server.dto.StaffDto;
import com.example.server.dto.StaffUpdateDto;
import com.example.server.entity.Staff;
import com.example.server.responsemodel.LoginRegisterResponse;
import com.example.server.responsemodel.ResponseBase;
import com.example.server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("staff")
@CrossOrigin
public class StaffController {

    @Autowired
    StaffService staffService;

    @PostMapping("add")
    public ResponseEntity<LoginRegisterResponse> addNewStaff(@RequestBody StaffDto staffDto){
        return ResponseEntity.ok(staffService.saveNewStaff(staffDto));
    }

    @PostMapping("adminAdd")
    public ResponseEntity<LoginRegisterResponse> addNewStaff(@RequestBody AdminStaffDto staffDto){
        return ResponseEntity.ok(staffService.saveNewStaffByAdmin(staffDto));
    }

    @GetMapping("home")
    public String getAppStatus(){
        return "Application running";
    }

    @GetMapping("all")
    public ResponseEntity<List<Staff>> getAllStaff(){
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("all-basic")
    public ResponseEntity<List<StaffBase>> getAllStaffBasic(){
        return ResponseEntity.ok(staffService.getAllStaffBasic());
    }

    @PutMapping("update")
    public ResponseEntity<ResponseBase> updateStaff(@RequestParam String staffId, @RequestBody StaffUpdateDto staffUpdateDto){
        ResponseBase response = new ResponseBase(staffService.updateStaff(staffId,staffUpdateDto),true);
        return ResponseEntity.ok(response);
    }

    @PutMapping("updateByUser")
    public ResponseEntity<Staff> updateStaffByUser(@RequestParam String staffId, @RequestBody StaffDto staffUpdateDto){
        return ResponseEntity.ok(staffService.updateStaffByUser(staffId,staffUpdateDto));
    }

    @DeleteMapping("del")
    public ResponseEntity<ResponseBase> deleteStaff(@RequestParam String staffId){
        ResponseBase response = new ResponseBase(staffService.deleteStaff(staffId),true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<LoginRegisterResponse> staffLogin(@RequestBody StaffDto staffDto){
        return ResponseEntity.ok(staffService.staffLogin(staffDto));
    }
}

