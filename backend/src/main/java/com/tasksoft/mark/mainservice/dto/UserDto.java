package com.tasksoft.mark.mainservice.dto;


import com.tasksoft.mark.mainservice.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;

    public static UserDto convert(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }
}
