package com.drmj.work_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "organizations")
@SQLDelete(sql = "UPDATE organizations SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
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