package pl.devzyra.restwebservice.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.devzyra.restwebservice.dto.UserDto;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);

}
