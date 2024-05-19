package com.nnk.springboot.config;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling user details retrieval in Spring Security.
 * This class is responsible for loading user-specific data and is used by the Spring Security framework
 * to perform authentication and authorization. It interacts with the application's data layer through
 * the {@link UserRepository} to fetch user information.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads the user's details based on the username provided. This method is called during the
     * authentication process to fetch details about the user from the database.
     *
     * @param username the username identifying the user whose data is to be loaded.
     * @return UserDetails containing necessary information such as username, password, and authorities.
     * @throws UsernameNotFoundException if no user is found with the provided username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole()));
    }

    /**
     * Helper method to convert a user's role into a list of {@link GrantedAuthority}.
     * Each role is prefixed with "ROLE_" to comply with Spring Security's role naming convention.
     *
     * @param role the role name associated with the user.
     * @return a list of GrantedAuthority objects containing the roles assigned to the user.
     */
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}


