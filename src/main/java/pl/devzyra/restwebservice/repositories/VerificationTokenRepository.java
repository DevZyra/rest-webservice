package pl.devzyra.restwebservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.model.entities.VerificationTokenEntity;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity,Long> {

    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUserEntity(UserEntity user);

}
