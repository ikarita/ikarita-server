package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.user.UserDto;
import com.github.ikarita.server.model.entities.user.LocalUser;
import com.github.ikarita.server.security.permissions.UserRole;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

class LocalUserMapperTest {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testUserToUserDto(){
        final LocalUser localUser = new LocalUser(
                1L,
                "John",
                "john@gmail.com",
                "secret",
                UserRole.VIEWER,
                Collections.emptySet(),
                false
        );

        final UserDto userDto = mapper.asDto(localUser);

        assertEquals(1L, userDto.getId());
        assertEquals("John", userDto.getUsername());
        assertEquals("john@gmail.com", userDto.getEmail());
        assertEquals(UserRole.VIEWER, userDto.getUserRole());
    }
}