package com.example.server.service;

import com.example.server.dto.AdminStaffDto;
import com.example.server.dto.StaffBase;
import com.example.server.dto.StaffDto;
import com.example.server.dto.StaffUpdateDto;
import com.example.server.entity.Staff;
import com.example.server.exception.InvalidDataException;
import com.example.server.exception.StaffNotFoundException;
import com.example.server.repository.StaffRepository;
import com.example.server.responsemodel.LoginRegisterResponse;
import com.example.server.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    NotificationService notificationService;

    public LoginRegisterResponse saveNewStaff(StaffDto staffDto){
        Staff staff = Mapper.staffMapper(staffDto);
        staff.setStaffPass(passwordEncoder.encode(staff.getStaffPass()));
        Staff savedStaff = staffRepository.save(staff);
        return new LoginRegisterResponse(
                savedStaff.getStaffId(),
                true,
                Staff.StaffRole.ROLE_EMPLOYEE,
                savedStaff.getStaffName()
        );
    }

    public List<Staff> getAllStaff(){
        return staffRepository.findAll();
    }

    public String deleteStaff(String staffId){
        staffRepository.deleteById(staffId);
        return "Staff Removed";
    }

    public String updateStaff(String staffId, StaffUpdateDto staffUpdateDto){
        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new StaffNotFoundException("No Staff Found with Id: "+staffId));
        String tempVal = staffUpdateDto.getStaffEmail();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffEmail(tempVal);
        }
        tempVal = staffUpdateDto.getStaffName();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffName(tempVal);
        }
        tempVal = staffUpdateDto.getStaffPass();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffPass(passwordEncoder.encode(tempVal));
        }
        tempVal = staffUpdateDto.getStaffRole();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffRole(Staff.StaffRole.valueOf(tempVal));
        }

        staffRepository.save(staff);
        return "Staff data updated";
    }


    public Staff updateStaffByUser(String staffId, StaffDto staffDto){
        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new StaffNotFoundException("No Staff Found with Id: "+staffId));
        String tempVal = staffDto.getStaffEmail();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffEmail(tempVal);
        }
        tempVal = staffDto.getStaffName();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffName(tempVal);
        }
        tempVal = staffDto.getStaffPass();
        if(tempVal != null && !tempVal.isEmpty()){
            staff.setStaffPass(passwordEncoder.encode(tempVal));
        }
        return staffRepository.save(staff);
    }



    public LoginRegisterResponse staffLogin(StaffDto staffDto){
        String mail = staffDto.getStaffEmail();
        String pass = staffDto.getStaffPass();
        if(mail != null && !mail.isEmpty() && pass != null && !pass.isEmpty()){
            boolean isExist = staffRepository.existsByStaffEmail(mail);
            if(!isExist) throw new StaffNotFoundException("No staff with email Id: ("+mail+") Check email Id.");
            Staff staff = staffRepository.findByStaffEmail(mail);
            String actualPass = staff.getStaffPass();
            boolean isValidPass = passwordEncoder.matches(pass, actualPass);
            if(isValidPass){
                return new LoginRegisterResponse(staff.getStaffId(),true,staff.getStaffRole(), staff.getStaffName());
            }
            else throw new InvalidDataException("Incorrect password: ("+pass+")");
        }else throw new InvalidDataException("Provide Proper details");
    }

    public List<Staff> getStaffsByIds(List<String> teamMembers) {
        return staffRepository.findAllById(teamMembers);
    }

    public Staff getStaffById(String teamLeader) {
        return staffRepository.findById(teamLeader).orElseThrow(() -> new StaffNotFoundException("No such staff with id: "+teamLeader));
    }

    public void createAdminAccount(){
        if(!staffRepository.existsByStaffEmail("admin@gmail.com")) {
            staffRepository.save(
                    new Staff(
                            "Admin:" + UUID.randomUUID(),
                            "Admin",
                            "admin@gmail.com",
                            passwordEncoder.encode("admin"),
                            Staff.StaffRole.ROLE_ADMIN
                    )
            );
        }
    }

    public LoginRegisterResponse saveNewStaffByAdmin(AdminStaffDto staffDto) {
        Staff staff = staffRepository.save(new Staff(
                "Staff:"+UUID.randomUUID(),
                staffDto.getStaffName(),
                staffDto.getStaffEmail(),
                passwordEncoder.encode(staffDto.getStaffPass()),
                staffDto.getStaffRole()

        ));
        return new LoginRegisterResponse(
                staff.getStaffId(),
                true,
                staff.getStaffRole(),
                staff.getStaffName()
        );
    }

    public List<StaffBase> getAllStaffBasic(){
        return getAllStaff().stream().map(st -> new StaffBase(
                st.getStaffId(),
                st.getStaffName()
        )).collect(Collectors.toList());
    }

    public void sendNotificationsToStaffs(List<String> staffIds,String message,String subject){
        staffIds.forEach(staffId ->{
            Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new StaffNotFoundException("No staff with Id : "+staffId));
            notificationService.sendEmail(staff.getStaffEmail(),subject,message);
        });
    }
}