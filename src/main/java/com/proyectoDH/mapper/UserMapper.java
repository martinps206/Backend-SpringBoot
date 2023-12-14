package com.proyectoDH.mapper;

import com.proyectoDH.dto.SignUpDto;
import com.proyectoDH.dto.UserDto;
import com.proyectoDH.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}

