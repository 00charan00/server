package com.example.server.repository;

import com.example.server.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff,String> {
    Staff findByStaffEmail(String username);

    boolean existsByStaffEmail(String mail);
}