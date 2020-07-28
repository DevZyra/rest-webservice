package pl.devzyra.restwebservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.devzyra.restwebservice.model.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {

    UserEntity findByEmail(String email);

    UserEntity findUserEntityByUserId(String userId);

}
