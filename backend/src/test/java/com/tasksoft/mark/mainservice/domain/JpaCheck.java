package com.tasksoft.mark.mainservice.domain;

import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import com.tasksoft.mark.mainservice.repository.GroupRepository;
import com.tasksoft.mark.mainservice.repository.TaskRepository;
import com.tasksoft.mark.mainservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class JpaCheck {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findUsersWhenExist(){
        var user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> userOptional = userRepository.findById(user.getId());
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().getUsername()).isEqualTo(user.getUsername());

    }

    @Test
    void checkTaskCreation(){
        var assigner = new User();
        assigner.setFirstName("John");
        assigner.setLastName("Doe");
        assigner.setUsername("johndoe");
        entityManager.persist(assigner);

        entityManager.flush();

        var assignee = new User();
        assignee.setFirstName("Mark");
        assignee.setLastName("D");
        assignee.setUsername("mark2909");
        entityManager.persist(assignee);

        entityManager.flush();

        Task task = new Task();
        task.setAssigner(assigner);
        task.setAssigneeUser(assignee);
        task.setName("test task");
        task.setDescription("test task description");
        task.setTaskType(TaskType.CREATED);
        entityManager.persist(task);

        entityManager.flush();
        entityManager.clear();

        Optional<Task> savedTask = taskRepository.findById(task.getId());
        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().getAssigner()).isNotNull();
        assertThat(savedTask.get().getAssigneeUser()).isNotNull();
        assertThat(savedTask.get().getAssigner().getId()).isEqualTo(assigner.getId());
        assertThat(savedTask.get().getAssigneeUser().getId()).isEqualTo(assignee.getId());

        Optional<User> savedAssigner = userRepository.findById(assigner.getId());
        Optional<User> savedAssignee = userRepository.findById(assignee.getId());

        assertThat(savedAssigner.isPresent()).isTrue();
        assertThat(savedAssignee.isPresent()).isTrue();

        assertThat(savedAssigner.get().getTasksCreated().size()).isEqualTo(1);
        assertThat(savedAssigner.get().getTasksCreated().iterator().next().getId()).isEqualTo(savedTask.get().getId());

        assertThat(savedAssignee.get().getTasksAssigned().size()).isEqualTo(1);
        assertThat(savedAssignee.get().getTasksAssigned().iterator().next().getId()).isEqualTo(task.getId());
    }

    @Test
    void shouldAddUserToGroup() {
        var user = new User();
        user.setFirstName("John");
        user.setUsername("johndoe");
        entityManager.persist(user);

        var group = new Group();
        group.setName("Test group");
        entityManager.persist(group);

        group.addMember(user);

        entityManager.flush();
        entityManager.clear(); // Clear cache to force DB read


        Optional<Group> savedGroup = groupRepository.findById(group.getId());
        assertThat(savedGroup).isPresent();
        assertThat(savedGroup.get().getMembers().size()).isEqualTo(1);
        assertThat(savedGroup.get().getMembers().get(0).getUsername()).isEqualTo("johndoe");

        Optional<User> savedUser = userRepository.findById(user.getId());
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getGroups().size()).isEqualTo(1);
        assertThat(savedUser.get().getGroups().get(0).getName()).isEqualTo("Test group");
    }

    @Test
    void shouldRemoveUserFromGroup() {
        var user = new User();
        user.setFirstName("John");
        user.setUsername("johndoe");

        var group = new Group();
        group.setName("Test group");

        group.addMember(user); // Add user

        entityManager.persist(user); // Persist both
        entityManager.persist(group);

        entityManager.flush();
        entityManager.clear();

        Optional<Group> savedGroup = groupRepository.findById(group.getId());
        assertThat(savedGroup).isPresent();
        assertThat(savedGroup.get().getMembers().size()).isEqualTo(1);

        Optional<User> savedUser = userRepository.findById(user.getId());
        assertThat(savedUser).isPresent();

        Group managedGroup = savedGroup.get();
        User managedUser = savedUser.get();

        managedGroup.removeMember(managedUser);

        entityManager.flush();
        entityManager.clear();

        Group finalGroup = groupRepository.findById(group.getId()).get();
        assertThat(finalGroup.getMembers().size()).isEqualTo(0);

        User finalUser = userRepository.findById(user.getId()).get();
        assertThat(finalUser.getGroups().size()).isEqualTo(0);
    }
}
