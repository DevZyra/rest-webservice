package pl.devzyra.restwebservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.devzyra.restwebservice.dto.AddressDto;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.dto.Utils;
import pl.devzyra.restwebservice.exceptions.UserServiceException;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static pl.devzyra.restwebservice.exceptions.ErrorMessages.RECORD_ALREADY_EXISTS;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Utils utils;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;


    public UserServiceImpl(UserRepository userRepository, Utils utils, PasswordEncoder passwordEncoder, ModelMapper modelMapper, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }



    @Override
    public UserDto createUser(UserDto user) {

        if( userRepository.findByEmail(user.getEmail()) != null){
            throw new UserServiceException(RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        for(int i=0;i<user.getAddresses().size();i++){
            AddressDto address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(20));
            user.getAddresses().set(i,address);
        }


        UserEntity userEntity = modelMapper.map(user,UserEntity.class);


        userEntity.setUserId(utils.generateUserId(15));
        userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));

        // set email verification to false
        userEntity.setEmailVerificationStatus(false);

        // create and save token
        try{
            String token = utils.generateVerificationToken(25);
            verificationTokenService.save(userEntity,token);

        // send verification email
        emailService.sendHtmlMail(userEntity);


        } catch (Exception e){
            e.printStackTrace();
        }


        UserEntity stored = userRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(stored,UserDto.class);

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
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(page,limit);
        Page<UserEntity> userPage = userRepository.findAll(pageableRequest);

        List<UserEntity> users = userPage.getContent();

        users.stream().forEach(x-> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(x,userDto);
            returnValue.add(userDto);
        });

        return returnValue;
    }

    @Override
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity userEntity = userRepository.findByEmail(email);
      if(userEntity == null){ throw new UsernameNotFoundException(String.format("User [EMAIL] -> %s",email)); }

      return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
