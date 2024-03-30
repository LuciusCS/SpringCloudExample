package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "auth_user_role")
@Data
public class AuthUserRole  implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true, updatable = true)
    private AuthUser user;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = true, updatable = true)
    private AuthRole role;
}
