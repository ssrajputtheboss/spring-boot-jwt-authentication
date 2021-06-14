package com.shash.jwtapp.service;

import java.util.ArrayList;

import com.shash.jwtapp.model.UserList;
import com.shash.jwtapp.repo.UserListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserListRepository repository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserList user = repository.findByUsername(username).orElse(null);
        if(user == null) throw new UsernameNotFoundException("User not found");
        return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
    }

    public boolean save(UserList user){
        user.setPassword(encoder.encode(user.getPassword()));
        if(repository.existsByUsername(user.getUsername()))
            return false;
        else{
            repository.save(user);
            return true;
        }
    }
    
}
