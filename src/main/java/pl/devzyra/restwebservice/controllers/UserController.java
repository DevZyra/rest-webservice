package pl.devzyra.restwebservice.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/{userId}" , produces = {MediaType.APPLICATION_JSON_VALUE,
                                                  MediaType.APPLICATION_XML_VALUE   })
    public UserRest getUser(@PathVariable String userId){

        UserRest returnVal = new UserRest();

        UserDto userDto = userService.getUserByUserId(userId);
        BeanUtils.copyProperties(userDto,returnVal);

        return returnVal;
    }


    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
                 produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
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
