package com.pepic.TravelPlanner.repositories;

import com.pepic.TravelPlanner.models.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    Privilege findByName(String name);
}
