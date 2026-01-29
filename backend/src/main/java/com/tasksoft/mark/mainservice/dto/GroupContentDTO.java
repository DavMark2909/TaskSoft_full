package com.tasksoft.mark.mainservice.dto;

import java.util.List;

public record GroupContentDTO(
        String groupName,
        List<GroupMemberDto> groupMembers
) {

}
