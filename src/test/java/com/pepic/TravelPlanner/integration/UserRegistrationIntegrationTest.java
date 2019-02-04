package com.pepic.TravelPlanner.integration;

import com.pepic.TravelPlanner.TestConfig;
import com.pepic.TravelPlanner.controllers.UserController;
import com.pepic.TravelPlanner.dto.UserDto;
import com.pepic.TravelPlanner.models.User;
import com.pepic.TravelPlanner.repositories.CommentRepository;
import com.pepic.TravelPlanner.repositories.RoleRepository;
import com.pepic.TravelPlanner.repositories.TripRepository;
import com.pepic.TravelPlanner.repositories.UserRepository;
import com.pepic.TravelPlanner.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.pepic.TravelPlanner.TravelPlannerApplication;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationIntegrationTest {
    @MockBean
    private UserService userService;

    @MockBean
    private UserController userController;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private TripRepository tripRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userService = new UserService(commentRepository, tripRepository, userRepository, roleRepository);
        this.userController = new UserController(this.userService);
    }

    @Test
    public void userRegistrationTest() throws Exception {
        when(this.userRepository.save(any(User.class))).thenReturn(new User());
        ResultActions resultActions = this.mockMvc.perform(post("/user/register", new UserDto("test", "test")));
        verify(this.userRepository, times(3)).save(any(User.class));

        resultActions.andExpect(status().is2xxSuccessful());
    }
}
