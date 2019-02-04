package com.pepic.TravelPlanner.controllers;

import com.pepic.TravelPlanner.dto.UserDto;
import com.pepic.TravelPlanner.services.AdminService;
import com.pepic.TravelPlanner.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager")
public class ManagerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private AdminService adminService;


    public ManagerController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserDto> create(@RequestBody UserDto user){
        try {
            String manager = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Creating new user [{}] as manager {}", user.getUsername(), manager);
            this.adminService.createUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestBody String user){
        try {
            String manager = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Deleting user [{}] as manager {}", user, manager);
            this.adminService.deleteUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
