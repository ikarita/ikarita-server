package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.LocalUser;
import com.github.ikarita.server.security.UserRole;
import com.google.common.collect.Sets;
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
                Sets.newHashSet(UserRole.VIEWER),
                Collections.emptySet(),
                false
        );

        final UserDto userDto = mapper.asDto(localUser);

        assertEquals(1L, userDto.getId());
        assertEquals("John", userDto.getUsername());
        assertEquals("john@gmail.com", userDto.getEmail());
        assertEquals(1, userDto.getUserRoles().size());
        assertEquals(UserRole.VIEWER, userDto.getUserRoles().stream().iterator().next());
    }
}