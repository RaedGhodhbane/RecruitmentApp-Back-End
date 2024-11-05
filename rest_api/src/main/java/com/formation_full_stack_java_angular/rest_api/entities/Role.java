package com.formation_full_stack_java_angular.rest_api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "USER_ROLE")
@Data @NoArgsConstructor @AllArgsConstructor
public class Role implements Serializable {

    private static final long SerialVersionUUID = -3247777786657672L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    private String name;

    @ManyToMany(mappedBy = "roles")
    @Transient
    private List<User> users;
}
