package com.pepic.TravelPlanner.services;

import com.pepic.TravelPlanner.dto.UserDto;
import com.pepic.TravelPlanner.models.User;
import com.pepic.TravelPlanner.repositories.CommentRepository;
import com.pepic.TravelPlanner.repositories.TripRepository;
import com.pepic.TravelPlanner.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TripRepository tripRepository;

    public AdminService(UserRepository userRepository,
                        UserService userService,
                        CommentRepository commentRepository,
                        TripRepository tripRepository)
    {
        this.userRepository = userRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.tripRepository = tripRepository;
    }

    public User createUser(UserDto userDto) {
        User user = this.userRepository.findByUsername(userDto.getUsername());
        if(user == null) {
            this.userService.register(userDto.getUsername(), userDto.getPassword());
        }
        return user;
    }

    public User deleteUser(String username) {
        User user = this.userRepository.findByUsername(username);
        this.userRepository.delete(user);
        return user;
    }


    public void deleteComment(Long id) {
        this.commentRepository.deleteById(id);
    }

    public void deleteTrip(Long id) {
        this.tripRepository.deleteById(id);
    }

}
