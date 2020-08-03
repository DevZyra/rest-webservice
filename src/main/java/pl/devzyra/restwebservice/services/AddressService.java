package pl.devzyra.restwebservice.services;

import pl.devzyra.restwebservice.dto.AddressDto;

import java.util.List;


public interface AddressService {

    List<AddressDto> getAddresses(String userId);

    AddressDto getSpecificAddress(String addressId);
}
