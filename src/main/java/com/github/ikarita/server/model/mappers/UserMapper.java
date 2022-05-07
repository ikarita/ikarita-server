package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.NewUserDto;
import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User newUserTdoToUser(NewUserDto newUserDto);
    UserDto userToUserDto(User user);
}
