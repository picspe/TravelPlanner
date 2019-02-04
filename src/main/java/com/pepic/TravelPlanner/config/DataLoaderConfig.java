package com.pepic.TravelPlanner.config;

import com.pepic.TravelPlanner.models.Privilege;
import com.pepic.TravelPlanner.models.Role;
import com.pepic.TravelPlanner.models.User;
import com.pepic.TravelPlanner.repositories.PrivilegeRepository;
import com.pepic.TravelPlanner.repositories.RoleRepository;
import com.pepic.TravelPlanner.repositories.UserRepository;
import com.pepic.TravelPlanner.utils.PrivilegeType;
import com.pepic.TravelPlanner.utils.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class DataLoaderConfig implements ApplicationListener<ContextRefreshedEvent>{


    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }


        final Privilege readTravelPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_TRAVEL.name());
        final Privilege writeTravelPrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_TRAVEL.name());
        final Privilege readManagerPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_MANAGER.name());
        final Privilege writeMagerPrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_MANAGER.name());
        final Privilege readAdminPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_ADMIN.name());
        final Privilege writeAdminPrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_ADMIN.name());


        final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(readAdminPrivilege,
                writeAdminPrivilege, readManagerPrivilege,writeMagerPrivilege, readTravelPrivilege, writeTravelPrivilege));
        final List<Privilege> managerPrivileges = new ArrayList<Privilege>(Arrays.asList(readManagerPrivilege,
                writeMagerPrivilege, readTravelPrivilege, writeTravelPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<Privilege>(Arrays.asList(readTravelPrivilege, writeTravelPrivilege));

        final Role adminRole = createRoleIfNotFound(RoleName.ADMIN.name(), adminPrivileges);
        createRoleIfNotFound(RoleName.ADMIN.name(), adminPrivileges);
        final Role managerRole = createRoleIfNotFound(RoleName.ADMIN.name(), managerPrivileges);
        createRoleIfNotFound(RoleName.MANAGER.name(), managerPrivileges);
        final Role userRole = createRoleIfNotFound(RoleName.ADMIN.name(), userPrivileges);
        createRoleIfNotFound(RoleName.USER.name(), userPrivileges);


        createUserIfNotFound("admin", "admin",  new ArrayList<Role>(Arrays.asList(adminRole)));
        createUserIfNotFound("manager", "manager",  new ArrayList<Role>(Arrays.asList(managerRole)));
        createUserIfNotFound("user", "user",  new ArrayList<Role>(Arrays.asList(userRole)));
        alreadySetup = true;
    }

    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    private final User createUserIfNotFound(final String username,  final String password, final Collection<Role> roles) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setPassword(passwordEncoder.encode(password));
            user.setUsername(username);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }
}
