package pl.devzyra.restwebservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.devzyra.restwebservice.dto.AddressDto;
import pl.devzyra.restwebservice.model.entities.AddressEntity;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.repositories.AddressRepository;
import pl.devzyra.restwebservice.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

        if(userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        addresses.forEach(a-> returnValue.add(modelMapper.map(a,AddressDto.class)));

        return returnValue;
    }

    @Override
    public AddressDto getSpecificAddress(String addressId) {
        AddressDto returnValue = new AddressDto();

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if(addressEntity != null){
            returnValue = modelMapper.map(addressEntity,AddressDto.class);
        }
        return returnValue;
    }
}
