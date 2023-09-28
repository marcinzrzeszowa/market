package com.mj.market.app.user;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    //TODO change to optional
    User findByUsername(String username);
    User save(User entity);
    List<User>findAll();
    Optional<User> findById(Long id);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);
}
