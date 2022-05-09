package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.NewUserDto;
import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.LocalUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    LocalUser asEntity(NewUserDto newUserDto);
    UserDto asDto(LocalUser localUser);
}
