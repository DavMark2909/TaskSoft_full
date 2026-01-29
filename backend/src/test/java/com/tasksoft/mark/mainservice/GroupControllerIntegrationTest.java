package com.tasksoft.mark.mainservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasksoft.mark.mainservice.dto.GroupCreateDto;
import com.tasksoft.mark.mainservice.dto.TaskCreateDto;
import com.tasksoft.mark.mainservice.dto.UserCreateDto;
import com.tasksoft.mark.mainservice.dto.UserToGroupDTO;
import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.entity.enums.NotificationType;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import com.tasksoft.mark.mainservice.repository.GroupRepository;
import com.tasksoft.mark.mainservice.repository.TaskRepository;
import com.tasksoft.mark.mainservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Transactional
class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testCreateUserEndpoint() throws Exception {
        UserCreateDto request = new UserCreateDto("test_user", "test", "user");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))

            .andExpect(status().isCreated());

    }

    @Test
    void testTaskCreationUserAssignment() throws Exception {
        User assinger = new User();
        assinger.setUsername("assinger");
        assinger.setFirstName("assinger");
        assinger.setLastName("assinger");
        User user1 = userRepository.save(assinger);

        User assingee = new User();
        assingee.setUsername("assingee");
        assingee.setFirstName("assingee");
        assingee.setLastName("assingee");
        User user2 = userRepository.save(assingee);

        TaskCreateDto dto = new TaskCreateDto("test task", "description", user1.getId(), user2.getId(), NotificationType.SINGLE);
        String requestJson = objectMapper.writeValueAsString(dto);

        MvcResult mvcResult = mockMvc.perform(post("/tasks/create").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Long returnedId = Long.valueOf(response);

        assertThat(returnedId).isNotNull();
        Optional<Task> savedTask = taskRepository.findById(returnedId);

        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().getName()).isEqualTo("test task");

        assertThat(savedTask.get().getAssigner()).isNotNull();
        assertThat(savedTask.get().getAssigneeUser()).isNotNull();
        assertThat(savedTask.get().getAssigner().getId()).isEqualTo(user1.getId());
        assertThat(savedTask.get().getAssigneeUser().getId()).isEqualTo(user2.getId());
    }

}
