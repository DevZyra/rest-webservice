package pl.devzyra.restwebservice.services;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.dto.Utils;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Utils utils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, Utils utils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public UserDto createUser(UserDto user) {

        if( userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException(" This email is already registered");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity);

        userEntity.setUserId(utils.generateUserId(15));
        userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));


        UserEntity stored = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(stored,returnValue);

        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity userEntity = userRepository.findByEmail(email);
      if(userEntity == null){ throw new UsernameNotFoundException(String.format("[USER] -> %s",email)); }

      return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
