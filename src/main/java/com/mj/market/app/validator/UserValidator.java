package com.mj.market.app.validator;

import com.mj.market.app.user.User;
import com.mj.market.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService){
        this.userService = userService;
    }
    @Override
    public boolean supports(Class<?> uClass) {
        return User.class.equals(uClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.user.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.user.password");

        if(user.getUsername().length() < 3 ) {
            errors.rejectValue("username", "error.user.username.toShort");
        }
        if(user.getUsername().length() > 20){
            errors.rejectValue("username","error.user.username.toLong");
        }

        if (user.getPassword().length() < 3) {
            errors.rejectValue("password", "error.user.password.toShort");
        }
        if (user.getPassword().length() > 100 ){
            errors.rejectValue("password", "error.user.password.toLong");
        }

        User repoUser = userService.findByUsername(user.getUsername());
        if (repoUser != null && repoUser.getId() != user.getId()) {
            errors.rejectValue("username", "error.user.username.duplicated");
        }

        repoUser = userService.findByEmail(user.getEmail());
        if (repoUser != null && repoUser.getId() != user.getId()){
            errors.rejectValue("email", "error.user.email.duplicated");
        }
    }
}
