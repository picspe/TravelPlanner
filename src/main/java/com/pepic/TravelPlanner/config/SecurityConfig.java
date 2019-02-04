package com.pepic.TravelPlanner.config;

import com.pepic.TravelPlanner.repositories.UserRepository;
import com.pepic.TravelPlanner.services.CustomUserDetailsService;
import com.pepic.TravelPlanner.utils.PrivilegeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        super();
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/login","/user/register")
                    .permitAll()
                .antMatchers("/user", "/user/**")
                    .hasAnyAuthority(PrivilegeType.READ_TRAVEL.name(), PrivilegeType.WRITE_TRAVEL.name())
                .antMatchers("/manager", "/manager/**")
                    .hasAnyAuthority(PrivilegeType.READ_MANAGER.name(), PrivilegeType.WRITE_MANAGER.name())
                .antMatchers("/admin", "/admin/**").
                    hasAnyAuthority(PrivilegeType.READ_ADMIN.name(), PrivilegeType.WRITE_ADMIN.name())
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(false)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(getCustomUserDetailsService());
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public UserDetailsService getCustomUserDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }
}
