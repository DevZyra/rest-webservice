package pl.devzyra.restwebservice.services;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.dto.Utils;
import pl.devzyra.restwebservice.exceptions.ErrorMessages;
import pl.devzyra.restwebservice.exceptions.UserServiceException;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.repositories.UserRepository;

import java.util.ArrayList;

import static pl.devzyra.restwebservice.exceptions.ErrorMessages.NO_RECORD_FOUND;
import static pl.devzyra.restwebservice.exceptions.ErrorMessages.RECORD_ALREADY_EXISTS;

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
            throw new UserServiceException(RECORD_ALREADY_EXISTS.getErrorMessage());
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
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null){ throw new UsernameNotFoundException(String.format("User [EMAIL] -> %s",email)); }
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

        if(userEntity == null){ throw new UsernameNotFoundException(String.format("User [ID] -> %s",userId)); }

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);

        return returnValue;

    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
       UserDto returnValue = new UserDto();
       UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

       if(userEntity == null) throw new UsernameNotFoundException(String.format("User [ID] -> %s not found",userId));

       if(user.getFirstName() != null ){
       userEntity.setFirstName(user.getFirstName()); }

       if(user.getLastName() != null ){
       userEntity.setLastName(user.getLastName());
       }

       if(user.getPassword() != null ){
       userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
       }

       UserEntity savedEntity = userRepository.save(userEntity);
       BeanUtils.copyProperties(savedEntity,returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
        if(userEntity == null){ throw new UsernameNotFoundException(String.format("User [ID]-> %s",userId)); }

        userRepository.delete(userEntity);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity userEntity = userRepository.findByEmail(email);
      if(userEntity == null){ throw new UsernameNotFoundException(String.format("User [EMAIL] -> %s",email)); }

      return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
