package com.demo.service;

import com.demo.entity.CustomUserDetails;
import com.demo.entity.User;
import com.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usOptional  = userRepository.findByUsername(username) ;
        if (!usOptional.isPresent()) {
            return null;
        }
        User user = usOptional.get();
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if (user.getUsername().equals("admin")){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new CustomUserDetails(user, authorities);
    }
}
