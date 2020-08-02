package pl.devzyra.restwebservice.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import pl.devzyra.restwebservice.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity(name = "addresses")
public class AddressEntity implements Serializable {


    private static final long serialVersionUID = 5713925759549617466L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30,nullable = false)
    private String addressId;    // safely shared id
    @Column(length = 30,nullable = false)
    private String city;
    @Column(length = 30,nullable = false)
    private String country;
    @Column(length = 100,nullable = false)
    private String streetName;
    @Column(length = 7,nullable = false)
    private String zipCode;
    @Column(length = 10,nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "users_id")
    @JsonBackReference
    private UserEntity userDetails;
}
