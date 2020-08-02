package pl.devzyra.restwebservice.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressesRest {

    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String zipCode;
    private String type;

}
