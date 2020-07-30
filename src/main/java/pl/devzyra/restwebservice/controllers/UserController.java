package pl.devzyra.restwebservice.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.exceptions.ErrorMessages;
import pl.devzyra.restwebservice.exceptions.UserServiceException;
import pl.devzyra.restwebservice.model.request.UserDetailsRequestModel;
import pl.devzyra.restwebservice.model.response.OperationStatus;
import pl.devzyra.restwebservice.model.response.UserRest;
import pl.devzyra.restwebservice.services.UserService;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static pl.devzyra.restwebservice.exceptions.ErrorMessages.MISSING_REQUIRED_FIELDS;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE   })
    public List<UserRest> getAllUsers(@RequestParam(value = "page",defaultValue ="0") int page,
                                      @RequestParam(value = "limit",defaultValue ="25") int limit){

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page,limit);

        users.stream().forEach(x-> {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(x,userModel);
            returnValue.add(userModel);
        });

        return returnValue;
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

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(MISSING_REQUIRED_FIELDS.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);

        UserDto createdUser = userService.createUser(userDto);

        BeanUtils.copyProperties(createdUser,returnValue);


        return returnValue;
    }


    @PutMapping(path ="/{userId}",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
                                  produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String userId){

        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);

        UserDto updatedUser = userService.updateUser(userId,userDto);

        BeanUtils.copyProperties(updatedUser,returnValue);

        return returnValue;
    }


    @DeleteMapping(value = "/{userId}", produces ={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public OperationStatus deleteUser(@PathVariable String userId){

        OperationStatus operationStatus = new OperationStatus();
        operationStatus.setMethod("DELETE");

        userService.deleteUser(userId);

        operationStatus.setStatus("SUCCESS");

        return operationStatus;
    }



}
