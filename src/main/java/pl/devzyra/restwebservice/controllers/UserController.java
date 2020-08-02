package pl.devzyra.restwebservice.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.devzyra.restwebservice.dto.AddressDto;
import pl.devzyra.restwebservice.dto.UserDto;
import pl.devzyra.restwebservice.exceptions.UserServiceException;
import pl.devzyra.restwebservice.model.request.UserDetailsRequestModel;
import pl.devzyra.restwebservice.model.response.AddressesRest;
import pl.devzyra.restwebservice.model.response.OperationStatus;
import pl.devzyra.restwebservice.model.response.UserRest;
import pl.devzyra.restwebservice.services.AddressService;
import pl.devzyra.restwebservice.services.UserService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static pl.devzyra.restwebservice.exceptions.ErrorMessages.MISSING_REQUIRED_FIELDS;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final ModelMapper modelMapper;


    public UserController(UserService userService, AddressService addressService, ModelMapper modelMapper) {
        this.userService = userService;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
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

      /*  UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);*/

        UserDto userDto = modelMapper.map(userDetails,UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);



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

    @GetMapping(value = "/{userId}/addresses" , produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE   })
    public List<AddressesRest> getUserAddresses(@PathVariable String userId){

        List<AddressesRest> returnValue = new ArrayList<>();

        List<AddressDto> addressDtoList = addressService.getAddresses(userId);

        if(addressDtoList != null && !addressDtoList.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            returnValue = modelMapper.map(addressDtoList, listType);
        }
        return returnValue;
    }
}
