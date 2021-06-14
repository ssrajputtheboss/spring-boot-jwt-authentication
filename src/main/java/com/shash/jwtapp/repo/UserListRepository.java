package com.shash.jwtapp.repo;

import java.util.Optional;

import com.shash.jwtapp.model.UserList;

import org.springframework.data.repository.CrudRepository;

public interface UserListRepository extends CrudRepository<UserList,Integer> {
    
    public Optional<UserList> findByUsername(String username);

    public boolean existsByUsername(String username);

}
