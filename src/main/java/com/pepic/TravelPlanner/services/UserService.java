package com.pepic.TravelPlanner.services;

import com.pepic.TravelPlanner.dto.TripDto;
import com.pepic.TravelPlanner.dto.UserDto;
import com.pepic.TravelPlanner.models.Comment;
import com.pepic.TravelPlanner.models.Trip;
import com.pepic.TravelPlanner.models.User;
import com.pepic.TravelPlanner.repositories.CommentRepository;
import com.pepic.TravelPlanner.repositories.RoleRepository;
import com.pepic.TravelPlanner.repositories.TripRepository;
import com.pepic.TravelPlanner.repositories.UserRepository;
import com.pepic.TravelPlanner.utils.RoleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserService(CommentRepository commentRepository,
                       TripRepository tripRepository,
                       UserRepository userRepository,
                       RoleRepository roleRepository)
    {
        this.commentRepository = commentRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    public UserDto register(String username, String password) {
        String encoded = new BCryptPasswordEncoder().encode(password);
        User user = new User(username, encoded);
        user.setRoles(Arrays.asList(roleRepository.findByName(RoleName.USER.name())));
        this.userRepository.save(user);
        return new UserDto(username, null);
    }

    public void addTrip(TripDto tripDto, String username) {
        User user = this.userRepository.findByUsername(username);
        Trip trip = new Trip(user, tripDto.getStart(), tripDto.getEnd(), tripDto.getDestination());

        this.tripRepository.save(trip);
    }

    public void addComment(String content, Long tripId, String username) {
        Comment comment = new Comment();
        User user = this.userRepository.findByUsername(username) ;
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if(trip.isPresent() && !content.isEmpty()) {
            comment.setUser(user);
            comment.setContent(content);
            comment.setTrip(trip.get());
            LOGGER.info("Saving comment [{}].", comment);
            this.commentRepository.save(comment);
        } else {
            LOGGER.warn("Trip with id {} not found.", tripId);
        }
    }

    public TripDto getTrip(Long tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);
        Collection<Comment> comments;
        if(trip.isPresent()) {
            comments = this.commentRepository.getAllCommentsForTrip(tripId);
            return new TripDto(trip.get(), comments);
        } else {
            LOGGER.warn("Trip with id {} not found.", tripId);
            return null;
        }
    }

    public Collection<TripDto> getAllTrips(String username) {
        return this.tripRepository.getAllTripsForUser(username)
                .stream()
                .map(trip -> {
                    TripDto tripDto = new TripDto(trip, Collections.EMPTY_LIST);
                    if(tripDto.getStart().isBefore(LocalDate.now())) {
                        tripDto.setDayCount(ChronoUnit.DAYS.between(
                                LocalDate.now().atStartOfDay(),
                                tripDto.getStart().atStartOfDay()));
                    }
                    return tripDto;
                })
                .collect(Collectors.toList());
    }

    public void deleteTripForUser(Long id, String username) {
        this.tripRepository.deleteByIdForUser(id, username);
    }

    public void deleteCommentForUser(Long id, String username) {
        this.commentRepository.deleteByIdForUser(id, username);
    }

    public Collection<Trip> searchByDestination(String destination) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.tripRepository.getAllTripsByDestination(username, destination);
    }

    public Iterable<Trip> searchByComment(String content) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Long> comments = this.commentRepository
                .getAllCommentsContaining(username, content).stream()
                .map(Comment::getId)
                .collect(Collectors.toList());

        return this.tripRepository.findAllById(comments);
    }
}
