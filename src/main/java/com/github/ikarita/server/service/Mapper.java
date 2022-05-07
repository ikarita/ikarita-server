package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.NewRoleDto;
import com.github.ikarita.server.model.dto.NewUserDto;
import com.github.ikarita.server.model.dto.RoleDto;
import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.Role;
import com.github.ikarita.server.model.entities.User;
import com.googlecode.jmapper.RelationalJMapper;
import com.googlecode.jmapper.api.JMapperAPI;

import static com.googlecode.jmapper.api.JMapperAPI.attribute;

public class Mapper {
    private Mapper() {}

    public static final RelationalJMapper<User> USER;
    public static final RelationalJMapper<Role> ROLE;

    static {
        USER = buildUserMapper();
        ROLE = buildRoleMapper();
    }

    private static RelationalJMapper<User> buildUserMapper(){
        JMapperAPI jMapperAPI = new JMapperAPI()
                .add(JMapperAPI.mappedClass(User.class)
                        .add(attribute("id")
                                .value("id")
                                .targetClasses(UserDto.class))
                        .add(attribute("username")
                                .value("username")
                                .targetClasses(UserDto.class, NewUserDto.class))
                        .add(attribute("email")
                                .value("email")
                                .targetClasses(UserDto.class, NewUserDto.class))
                        .add(attribute("password")
                                .value("password")
                                .targetClasses(NewUserDto.class))
                        .add(attribute("roles")
                                .value("roles")
                                .targetClasses(UserDto.class))
                );

        return new RelationalJMapper<>(User.class, jMapperAPI);
    }

    private static RelationalJMapper<Role> buildRoleMapper(){
        JMapperAPI jMapperAPI = new JMapperAPI()
                .add(JMapperAPI.mappedClass(Role.class)
                        .add(attribute("id")
                                .value("id")
                                .targetClasses(RoleDto.class))
                        .add(attribute("name")
                                .value("name")
                                .targetClasses(RoleDto.class, NewRoleDto.class))
                );

        return new RelationalJMapper<>(Role.class, jMapperAPI);
    }
}
