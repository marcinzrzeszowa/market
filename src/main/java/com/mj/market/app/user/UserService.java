package com.mj.market.app.user;

import com.mj.market.app.notifier.EmailService;
import com.mj.market.app.user.registration.Token;
import com.mj.market.app.user.registration.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final long ADMIN_INIT_ID = 1L;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService registrationTokenService;
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
        if(id != ADMIN_INIT_ID) userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        User u = findById(user.getId());
        u.setEmail(user.getEmail());
        u.setUsername(user.getUsername());
        u.setEnabled(user.getEnabled());
        u.setLocked(user.getLocked());
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

        Token token = registrationTokenService.createToken(user);
        emailService.sendRegistrationToken(token);
    }

    public void resetPassword(User user, User formUser) {
        String codedPassword = passwordEncoder.encode(formUser.getPassword());
        user.setPassword(codedPassword);
        userRepository.save(user);
    }

    public void enableUser(User user){
        user.setEnabled(true);
        userRepository.save(user);
    }
}
