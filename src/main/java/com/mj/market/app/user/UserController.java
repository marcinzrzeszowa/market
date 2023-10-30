package com.mj.market.app.user;

import com.mj.market.app.user.registration.TokenService;
import com.mj.market.app.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserValidator userValidator;
    private final TokenService registrationTokenService;


    @GetMapping("/login")
    public String login(Model model, String error){
        if (error != null){
            model.addAttribute("error", "Podaj poprawny Login i hasło");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, String error){
        if (error != null){
            model.addAttribute("error", "Nie wylogowano");
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
        userService.register(user);
        return "redirect:/users";
    }

    @GetMapping("/register/confirm/{token}")
    public String confirmRegisterToken(@PathVariable("token") String token, Model model){
        User user = registrationTokenService.confirmRegistrationToken(token);
        String communicate = "Nie zarejestrowano użytkownika. Błędny token";
        if(user != null){
            userService.enableUser(user);
            communicate = "Zarejestrowano użytkownika " + user.getUsername();
        }
        model.addAttribute("communicate", communicate);
        return "confirm";
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
        }
        return "error/404";
    }

    @PostMapping("/user/{id}")
    public String editUser(@ModelAttribute("user") @Valid User formUser,
                           BindingResult bindingResult){
        userValidator.validate(formUser,bindingResult);
        if(bindingResult.hasErrors()){
            logger.error(String.valueOf(bindingResult.getFieldError()));
        }
        userService.updateUser(formUser);
        return "redirect:/home";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteAlert(@PathVariable("id") Long id){
        if(userService.findById(id) != null) {
            userService.deleteUser(id);
            return "redirect:/users";
        }
        return "error/404";
    }

    @PostMapping("/user/password/{id}")
    public String passwordReset(@ModelAttribute("user_obj") User forumUser, @PathVariable Long id) {
        User user = userService.findById(id);
        if( user != null) {
            userService.resetPassword(user, forumUser);
            return "redirect:/home";
        }
        return "redirect:/home";
    }

    @GetMapping("/user/password/{id}")
    public String changeUserPassword(@PathVariable("id") Long id, Model model){
        User user = userService.findById(id);
        if(user != null) {
            model.addAttribute("auth_user", user);
            return "reset_password";
        }
        return "error/404";
    }


}
