package com.luxcrm.entities;

import com.luxcrm.enums.Role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @Column(nullable = false)
    private String status = "active";

}
