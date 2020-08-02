package pl.devzyra.restwebservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {

    private Long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String zipCode;
    private String type;
    private UserDto userDetails;
}
