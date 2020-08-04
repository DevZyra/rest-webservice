package pl.devzyra.restwebservice.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="verification_token")
@NoArgsConstructor
@Getter
@Setter
public class VerificationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "exp_date")
    private Timestamp expirationDate;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    public VerificationTokenEntity(UserEntity userEntity, String token) {
        this.userEntity = userEntity;
        this.token = token;
    }



}
