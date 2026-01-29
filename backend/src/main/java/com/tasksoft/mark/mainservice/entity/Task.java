package com.tasksoft.mark.mainservice.entity;

import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @CreationTimestamp // Automatically sets time on INSERT
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "due_date") // Can be null if no deadline exists
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "assigner_id", nullable = false)
    private User assigner;

    @ManyToOne
    @JoinColumn(name = "assignee_user_id", nullable = true)
    private User assigneeUser;

    @ManyToOne
    @JoinColumn(name = "assignee_group_id", nullable = true)
    private Group assigneeGroup;

    public void setGroup(Group group) {
        this.assigneeGroup = group;
        if (group.getTasks() == null){
            group.setTasks(new HashSet<>());
        }
        group.getTasks().add(this);
    }

    public void setUser(User user) {
        this.assigneeUser = user;
        if (user.getTasksAssigned() == null){
            user.setTasksAssigned(new HashSet<>());
        }
        user.getTasksAssigned().add(this);
    }
}
