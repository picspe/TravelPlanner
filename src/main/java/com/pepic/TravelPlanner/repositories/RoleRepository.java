package com.pepic.TravelPlanner.repositories;

import com.pepic.TravelPlanner.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
