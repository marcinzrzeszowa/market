package com.mj.market.app.user;

import com.mj.market.app.notifier.EmailService;
import com.mj.market.app.user.registration.RegistrationToken;
import com.mj.market.app.user.registration.RegistrationTokenRepository;
import com.mj.market.app.user.registration.RegistrationTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationTokenService registrationTokenService;
    private final EmailService emailService;

    public User findByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        if(user != null && findByUsername(user.getUsername()) == null)
            userRepository.save(user);
    }

    public User findById(Long id) throws UsernameNotFoundException{
        return userRepository.findById(id).get();
    }

    public void deleteUser(Long id) throws UsernameNotFoundException {
        //initialization Admin ID cant be deleted
        Long initAdminId = Long.valueOf(1);
        if(id != initAdminId) userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        User u = findById(user.getId());
        u.setEmail(user.getEmail());
        u.setUsername(user.getUsername());
        userRepository.save(u);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void register(User user) {
        String codedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(codedPassword);
        user.setRole(Role.USER);
        saveUser(user);

        RegistrationToken token = registrationTokenService.createRegistrationToken(user);
        emailService.sendRegistrationToken(token);
    }

    public void enableUser(User user){
        user.setEnabled(true);
        userRepository.save(user);
    }
}
