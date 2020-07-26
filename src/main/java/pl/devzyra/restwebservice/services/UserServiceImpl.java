package pl.devzyra.restwebservice.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.dto.Utils;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Utils utils;

    public UserServiceImpl(UserRepository userRepository, Utils utils) {
        this.userRepository = userRepository;
        this.utils = utils;
    }



    @Override
    public UserDto createUser(UserDto user) {

        if( userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException(" This email is already registered");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity);

        userEntity.setUserId(utils.generateUserId(15));
        userEntity.setEncryptedPassword("test");


        UserEntity stored = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(stored,returnValue);

        return returnValue;
    }




}
