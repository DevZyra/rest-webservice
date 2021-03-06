package pl.devzyra.restwebservice.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.model.entities.UserEntity;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);

    UserDto updateUser(String userId,UserDto userDto);

    void deleteUser(String userId);

    List<UserDto> getUsers(int page , int limit);

    void save(UserEntity userEntity);

}
