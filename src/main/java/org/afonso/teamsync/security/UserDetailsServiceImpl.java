package org.afonso.teamsync.security;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.repository.ManagerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Manager not found: " + username));
    }
}