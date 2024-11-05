package com.formation_full_stack_java_angular.rest_api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "utilisateur")
@Data @NoArgsConstructor @AllArgsConstructor
public class Utilisateur implements Serializable {
    private static final long SerialVersionUUID = -32227755586657672L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Long age;
    private String poste;
    private String competences;
    private String note;
    private String image;
}
