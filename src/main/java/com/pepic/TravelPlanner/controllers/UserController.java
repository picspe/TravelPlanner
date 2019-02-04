package com.pepic.TravelPlanner.controllers;

import com.pepic.TravelPlanner.dto.CommentDto;
import com.pepic.TravelPlanner.dto.TripDto;
import com.pepic.TravelPlanner.dto.UserDto;
import com.pepic.TravelPlanner.models.Comment;
import com.pepic.TravelPlanner.models.Trip;
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
import java.util.Collection;


@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserDto> register(@RequestBody UserDto user){
        try {
            LOGGER.info("New register request for user [{}]", user.getUsername());
            this.userService.register(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/trip", method = RequestMethod.POST)
    public ResponseEntity<TripDto> addTrip(@RequestBody TripDto trip){
        try {
            LOGGER.info("Adding new trip [{}]", trip);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.userService.addTrip(trip, username);
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/trip/delete", method = RequestMethod.POST)
    public ResponseEntity<TripDto> deleteTrip(@RequestBody TripDto trip){
        try {
            LOGGER.info("Deleting trip [{}]", trip);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.userService.deleteTripForUser(trip.getId(), username);
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/trip/all", method = RequestMethod.GET)
    public ResponseEntity<Collection<TripDto>> getAllTrips(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Collection<TripDto> trips = this.userService.getAllTrips(username);
            return new ResponseEntity<>(trips, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto comment){
        try {
            LOGGER.info("Adding new comment [{}]", comment);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.userService.addComment(comment.getContent(), comment.getTripId(), username);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST)
    public ResponseEntity<Comment> deleteComment(@RequestBody Comment comment){
        try {
            LOGGER.info("Deleting comment [{}]", comment);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.userService.deleteCommentForUser(comment.getId(), username);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/search/destination", method = RequestMethod.POST)
    public ResponseEntity<Collection<Trip>> searchByDestination(@RequestBody String destination){
        try {
            LOGGER.info("Searching for trips by destination [{}]", destination);
            Collection<Trip> trips = this.userService.searchByDestination(destination);
            return new ResponseEntity<>(trips, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/search/comment", method = RequestMethod.POST)
    public ResponseEntity<Iterable<Trip>> searchByComment(@RequestBody String comment){
        try {
            LOGGER.info("Searching for trips by comment containing [{}]", comment);
            Iterable<Trip> trips = this.userService.searchByComment(comment);
            return new ResponseEntity<>(trips, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
