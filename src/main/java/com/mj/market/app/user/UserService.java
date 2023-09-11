package com.mj.market.app.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        if(findByUsername(user.getUsername()) == null) userRepository.save(user);
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
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("No user with this email"));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                ()-> new UsernameNotFoundException("No user with this email"));
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getName())
                .email(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }
}
