package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.UserDto;
import com.github.ikarita.server.model.entities.Role;
import com.github.ikarita.server.model.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class UserMapperTest {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testUserToUserDto(){
        final User user = new User(
                1L,
                "John",
                "john@gmail.com",
                "secret",
                List.of(new Role(1L, "ROLE_USER"))
        );

        final UserDto userDto = mapper.userToUserDto(user);

        assertEquals(1L, userDto.getId());
        assertEquals("John", userDto.getUsername());
        assertEquals("john@gmail.com", userDto.getEmail());
        assertEquals(1, userDto.getRoles().size());
        assertEquals("ROLE_USER", userDto.getRoles().stream().iterator().next().getName());
    }
}