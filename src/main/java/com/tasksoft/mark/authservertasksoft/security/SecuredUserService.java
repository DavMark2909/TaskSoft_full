package com.tasksoft.mark.authservertasksoft.security;

import com.tasksoft.mark.authservertasksoft.dto.UserRegistrationDto;
import com.tasksoft.mark.authservertasksoft.entity.User;
import com.tasksoft.mark.authservertasksoft.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecuredUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecuredUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new SecurityUser(user);
    }

    public Optional<User> registerUser(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setAuthorities("employee");
        return Optional.of(userRepository.save(user));
    }
}
