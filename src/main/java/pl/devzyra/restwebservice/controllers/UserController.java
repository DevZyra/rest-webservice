package pl.devzyra.restwebservice.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.model.request.UserDetailsRequestModel;
import pl.devzyra.restwebservice.model.response.UserRest;
import pl.devzyra.restwebservice.services.UserService;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(){
        return "[GET] user called";
    }


    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){

        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);

        UserDto createdUser = userService.createUser(userDto);

        BeanUtils.copyProperties(createdUser,returnValue);


        return returnValue;
    }


    @PutMapping
    public String updateUser(){
        return "[PUT] user called";
    }


    @DeleteMapping
    public String deleteUser(){
        return "[DELETE] user called";
    }



}
