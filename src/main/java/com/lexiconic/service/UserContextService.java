package com.lexiconic.service;

import com.lexiconic.domain.entity.Users;
import com.lexiconic.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {
    private final UserRepository userRepository;

    public UserContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            Users currentUser = userRepository.findByEmail(userDetails.getUsername());
            if (currentUser != null) {
                return currentUser;
            }
            throw new UsernameNotFoundException("Current user not found in database");
        }
        return null;
    }

    public Users getCurrentUserOrThrow() {
        Users user = getCurrentUser();
        if (user == null) {
            throw new AccessDeniedException("User not logged in");
        }
        return user;
    }
}
