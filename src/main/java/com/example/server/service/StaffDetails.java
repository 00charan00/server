package com.example.server.service;

import com.example.server.entity.Staff;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class StaffDetails implements UserDetails {

    Staff staff;

    public StaffDetails(Staff staff){
        this.staff = staff;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(staff.getStaffRole().toString()));
    }

    @Override
    public String getPassword() {
        return staff.getStaffPass();
    }

    @Override
    public String getUsername() {
        return staff.getStaffEmail();
    }
}
