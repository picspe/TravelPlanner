package com.pepic.TravelPlanner.repositories;

import com.pepic.TravelPlanner.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM users" +
            " WHERE username = :username", nativeQuery = true)
    User findByUsername(String username);

}
