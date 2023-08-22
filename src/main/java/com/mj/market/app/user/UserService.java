package com.mj.market.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    //For Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    public void saveUser(User user) {
        user.setPassword(UserService.passwordEncoder().encode(user.getPassword()));
        user.setRole("ROLE_MODERATOR");
        userRepository.save(user);
    }
    public User findById(Long id) throws UsernameNotFoundException{
        return userRepository.findById(id).get();
    }

    public void deleteUser(Long id) throws UsernameNotFoundException {
        //initialization Admin ID cant be deleted
        Long initAdminId = Long.valueOf(1);
        if(id != initAdminId) {
            {
                userRepository.deleteById(id);
            }
        }
    }

    public void updateUser(User user) {
        User u = findById(user.getId());
        u.setEmail(user.getEmail());
        u.setUsername(user.getUsername());
        userRepository.save(u);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
