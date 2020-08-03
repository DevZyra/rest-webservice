package pl.devzyra.restwebservice.repositories;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import pl.devzyra.restwebservice.model.entities.AddressEntity;
import pl.devzyra.restwebservice.model.entities.UserEntity;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {

    List<AddressEntity> findAllByUserDetails(UserEntity userDetails);

    AddressEntity findByAddressId(String addressId);
}
