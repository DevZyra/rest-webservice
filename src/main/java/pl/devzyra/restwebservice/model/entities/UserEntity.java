package pl.devzyra.restwebservice.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class UserEntity implements Serializable {


    private static final long serialVersionUID = 5370386224448597536L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(/* unique = true, */ nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @OneToOne
    private VerificationTokenEntity emailVerificationToken;

    @Column(nullable = false/* , columnDefinition = "boolean default false" */)
    private Boolean emailVerificationStatus = false;

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AddressEntity> addresses;
}
