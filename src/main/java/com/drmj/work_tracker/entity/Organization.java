package com.drmj.work_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
}