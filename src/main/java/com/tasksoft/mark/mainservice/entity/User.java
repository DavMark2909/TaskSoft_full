package com.tasksoft.mark.mainservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    Long id;

    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "assigner")
    private Set<Task> tasksCreated = new HashSet<>();

    @OneToMany(mappedBy = "assigneeUser")
    private Set<Task> tasksAssigned = new HashSet<>();

    @OneToMany(mappedBy = "recipient")
    private Set<Notification> notifications = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    private List<Group> groups = new ArrayList<>();
}
