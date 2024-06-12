package com.getsmarter.model;

import com.getsmarter.dto.UserDto;
import com.getsmarter.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserModel {

    private ModelMapper modelMapper = new ModelMapper();

    public UserDto mapUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }


    //Methode pour convertir le DTO en entite
    public User mapDTOToInstructor(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
