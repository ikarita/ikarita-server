package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.user.UserDto;
import com.github.ikarita.server.model.entities.user.LocalUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto asDto(LocalUser localUser);
}
