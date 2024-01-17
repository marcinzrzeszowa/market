package com.mj.market.app.pricealert;

import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.user.Role;
import com.mj.market.app.user.User;
import com.mj.market.app.user.UserService;
import com.mj.market.app.validator.PriceAlertValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
public class PriceAlertController {
    private static final Logger logger = LoggerFactory.getLogger(PriceAlertController.class);
    private  PriceAlertService priceAlertService;
    private  UserService userService;
    private  PriceAlertValidator priceAlertValidator;

    @GetMapping("/alerts")
    public String showUserAlerts(Model model, Authentication authentication){
        User user = userService.findByUsername(authentication.getName());
        if(user.getRole().equals(Role.ROLE_ADMIN)){
            model.addAttribute("alerts", priceAlertService.readAllPriceAlerts());
            return "price_alerts";
        }
        model.addAttribute("alerts", priceAlertService.readUserPriceAlerts(user.getId()));
        return "price_alerts";
    }

    @GetMapping("/alerts/new")
    public String newAlert(Model model, Authentication authentication) {
            User user = userService.findByUsername(authentication.getName());
            PriceAlert priceAlert = new PriceAlert();
            priceAlert.setUser(user);
            List<Symbol> symbols = priceAlertService.getSymbols();
                model.addAttribute("alertForm", priceAlert);
                model.addAttribute("symbols", symbols);
        return "price_alert_new";
    }

    @PostMapping("/alerts/new")
    public String addAlert(@ModelAttribute("alertForm") PriceAlert priceAlert, BindingResult bindingResult, Model model){
        priceAlertValidator.validate(priceAlert, bindingResult);
            if(bindingResult.hasErrors()){
                logger.error(String.valueOf(bindingResult.getFieldError()));
                List<Symbol> symbols = priceAlertService.getSymbols();
                model.addAttribute("alertForm", priceAlert);
                model.addAttribute("symbols", symbols);
                return "price_alert_new";
            }
            priceAlertService.savePriceAlert(priceAlert);
            logger.debug(String.format("Product with id: %s successfully created.", priceAlert.getId()));
            return "redirect:/alerts";
    }

    @GetMapping("/alerts/edit/{id}")
    public String editAlert(@PathVariable("id") Long id, Model model){
        PriceAlert alert = priceAlertService.findById(id);
        List<Symbol> symbols = priceAlertService.getSymbols();
        if (alert != null){
            model.addAttribute("alertForm", alert);
            model.addAttribute("symbols", symbols);
            return "price_alert";
        }else {
            return "error/404";
        }
    }

    @PostMapping("/alerts/edit/{id}")
    public String updateAlert(@PathVariable("id") Long id,
                              @ModelAttribute("alertForm") @Valid PriceAlert alert,
                              BindingResult bindingResult,
                              Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("action", "editAlert");
            return "price_alert";
        }
        priceAlertService.updatePriceAlert(id, alert);
        return "redirect:/alerts";
        }

    @GetMapping("/alerts/delete/{id}")
    public String deleteAlert(@PathVariable("id") Long id){
        if(id!=null){
            priceAlertService.deletePriceAlert(id);
            return "redirect:/alerts";
        } else {
            return "error/404";
        }
    }
}

