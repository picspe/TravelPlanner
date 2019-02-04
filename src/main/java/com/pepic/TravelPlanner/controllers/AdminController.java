package com.pepic.TravelPlanner.controllers;

import com.pepic.TravelPlanner.dto.CommentDto;
import com.pepic.TravelPlanner.dto.TripDto;
import com.pepic.TravelPlanner.dto.UserDto;
import com.pepic.TravelPlanner.models.Comment;
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
@RequestMapping("admin")
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }


    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResponseEntity<UserDto> create(@RequestBody UserDto user){
        try {
            String admin = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Creating new user [{}] as admin {}", user.getUsername(), admin);
            this.adminService.createUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestBody String user) {
        try {
            String admin = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Deleting user [{}] as admin {}", user, admin);
            this.adminService.deleteUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/trip", method = RequestMethod.POST)
    public ResponseEntity<TripDto> addTrip(@RequestBody TripDto trip){
        try {
            String admin = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Adding new trip [{}] as admin {}", trip, admin);
            this.userService.addTrip(trip, trip.getUsername());
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/trip/delete", method = RequestMethod.POST)
    public ResponseEntity<TripDto> deleteTrip(@RequestBody TripDto trip){
        try {
            String admin = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Deleting trip [{}] as admin {}", trip, admin);
            this.adminService.deleteTrip(trip.getId());
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto comment){
        try {
            String admin = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Adding new comment [{}] as admin", comment, admin);
            this.userService.addComment(comment.getContent(), comment.getTripId(), comment.getAuthor());
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST)
    public ResponseEntity<Comment> deleteComment(@RequestBody Comment comment){
        try {
            String admin = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.info("Deleting comment [{}] as admin {}", comment, admin);
            this.adminService.deleteComment(comment.getId());
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
