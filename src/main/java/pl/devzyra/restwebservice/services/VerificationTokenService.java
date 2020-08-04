package pl.devzyra.restwebservice.services;

import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.model.entities.VerificationTokenEntity;


public interface VerificationTokenService {

    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUserEntity(UserEntity user);

    public void save(UserEntity user, String token);

}
