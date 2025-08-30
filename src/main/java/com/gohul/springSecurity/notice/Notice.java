package com.gohul.springSecurity.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    private Long noticeId;
    private String noteSummery;
    private String noteDetails;
    private LocalDate noticeBeginDate;
    private LocalDate noticeEndDate;
    @JsonIgnore
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @JsonIgnore
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

}
