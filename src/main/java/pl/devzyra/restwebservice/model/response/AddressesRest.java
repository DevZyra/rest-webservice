package pl.devzyra.restwebservice.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter                                                 // no need to extend when ret. EntityModel.of()
public class AddressesRest  /* extends RepresentationModel<AddressesRest> */{

    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String zipCode;
    private String type;

}
