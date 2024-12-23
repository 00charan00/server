package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProgressInfo {
    @Id
    String progressInfoId;
    String comment;
    @CreationTimestamp
    Timestamp updatedTime;
    @CreatedBy
    String updatedUser;

    @OneToMany(targetEntity = ImageDetail.class,fetch = FetchType.EAGER)
    List<ImageDetail> references = new ArrayList<>();

    Task.ProgressStatus progressStatus;

}
