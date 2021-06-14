package com.shash.jwtapp.service;

import java.util.ArrayList;

import com.shash.jwtapp.model.UserList;
import com.shash.jwtapp.repo.UserListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserListRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserList user = repository.findByUsername(username).orElse(null);
        if(user == null) return null;
        return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
    }

    public void save(UserList user){
        repository.save(user);
    }
    
}
