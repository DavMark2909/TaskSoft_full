package com.tasksoft.mark.mainservice.controllers;

import com.tasksoft.mark.mainservice.dto.*;
import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.security.SecurityUtils;
import com.tasksoft.mark.mainservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final SecurityUtils securityUtils;

    public GroupController(GroupService groupService, SecurityUtils securityUtils) {
        this.groupService = groupService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/user-groups")
    public ResponseEntity<List<GroupContentDTO>> getAllUserGroups() {
        Long userId = securityUtils.getCurrentUserId();
        List<GroupContentDTO> list = groupService.getUserGroups(userId).stream().map(this::mapGroupToDTO).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GroupDto>> getGroups() {
        List<GroupDto> allGroups = groupService.getAllGroups();
        return ResponseEntity.ok(allGroups);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<GroupDashboardDTO> getGroupStats(@PathVariable Long id) {
        GroupDashboardDTO dto = groupService.getGroupDashboard(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createGroup(@RequestBody GroupCreateDto groupCreateDto) {
        Group group = groupService.createGroup(groupCreateDto);
        return new ResponseEntity<>(group.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/add-user")
    public ResponseEntity<Long> addUser(@RequestBody UserToGroupDTO userToGroupDTO) {
        groupService.addUserToGroup(userToGroupDTO.groupId(), userToGroupDTO.userId());
        return new ResponseEntity<>(userToGroupDTO.groupId(), HttpStatus.OK);
    }

    @PostMapping("/remove-user")
    public ResponseEntity<Long> removeUser(@RequestBody UserToGroupDTO userToGroupDTO) {
        groupService.removeUserFromGroup(userToGroupDTO.groupId(), userToGroupDTO.userId());
        return new ResponseEntity<>(userToGroupDTO.groupId(), HttpStatus.OK);
    }

    private GroupContentDTO mapGroupToDTO(Group group) {
        List<GroupMemberDto> members = group.getMembers().stream()
                .map(user -> new GroupMemberDto(user.getId(), user.getUsername(), user.getFirstName() + " " + user.getLastName())).toList();
        return new GroupContentDTO(group.getName(), members);
    }



}
