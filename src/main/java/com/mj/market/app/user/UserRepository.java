package com.mj.market.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User save(User entity);
    List<User>findAll();
    Optional<User> findById(Long id);
    void deleteById(Long id);
    User findByEmail(String email);
}
