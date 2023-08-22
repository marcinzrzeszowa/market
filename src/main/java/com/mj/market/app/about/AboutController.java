package com.mj.market.app.about;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    public AboutController(){
    }

    @GetMapping(value = {"/about"})
    public String showAbout(){
        return "about";
    }

}
