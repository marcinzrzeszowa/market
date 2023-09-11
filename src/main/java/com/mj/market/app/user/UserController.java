package com.mj.market.app.user;

import com.mj.market.app.validator.UserValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserValidator userValidator;



    @PostMapping("/reg")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(userService.authenticate(request));
    }



    @GetMapping("/login")
    public String login(Model model, String error){
        if (error != null){
            model.addAttribute("error", "Podaj poprawny Login i hasło");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("userForm") @Valid User user,
                               BindingResult bindingResult) {
        userValidator.validate(user,bindingResult);
        if(bindingResult.hasErrors()){
            logger.error(String.valueOf(bindingResult.getFieldError()));
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        List<User> usersList= userService.getUsers();
        if(!usersList.isEmpty()){
            model.addAttribute("usersList",usersList );
            return "users";
        }
        return "error/403";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "user";
        }else {
            return "error/404";
        }
    }

    @PostMapping("/user/{id}")
    public String editUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult){
        userValidator.validate(user,bindingResult);
        if(bindingResult.hasErrors()){
            logger.error(String.valueOf(bindingResult.getFieldError()));
        }
        userService.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteAlert(@PathVariable("id") Long id){
        if(userService.findById(id) != null){
            userService.deleteUser(id);
            return "redirect:/users";
        }else {
            return "error/404";
        }
    }
}
