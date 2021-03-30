package com.velheor.internship.repository;

import com.velheor.internship.models.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {

    @Override
    List<User> findAll();

    Optional<User> findByEmail(String email);
}
