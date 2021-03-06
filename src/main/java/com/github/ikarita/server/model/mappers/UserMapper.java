package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.user.NewUserDto;
import com.github.ikarita.server.model.dto.user.UserDto;
import com.github.ikarita.server.model.entities.user.LocalUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userRole", constant = "VIEWER")
    LocalUser asEntity(NewUserDto newUserDto);
    UserDto asDto(LocalUser localUser);
}
