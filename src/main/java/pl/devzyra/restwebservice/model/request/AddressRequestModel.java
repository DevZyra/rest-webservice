package pl.devzyra.restwebservice.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestModel {

    private String city;
    private String country;
    private String streetName;
    private String zipCode;
    private String type;

}
