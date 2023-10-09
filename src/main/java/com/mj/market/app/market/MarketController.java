package com.mj.market.app.market;


import com.mj.market.app.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
@Controller
public class MarketController {

    private final MarketService marketService;


    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }


    @GetMapping(value = {"/symbols"})
    public String getSymbolsPrice(Model model){
        model.addAttribute("symbols",  marketService.getAllPrices());
        return "symbols";
    }

   /* @GetMapping("/symbols/{symbol}")
    public String getPriceDetails(
            @PathVariable String symbol,
            @RequestParam String interval,
            @RequestParam String beginDateTime,
            @RequestParam String finishDateTime,
            @RequestParam String limit, Model model){
        model.addAttribute("symbol",  marketService.getPriceDetails(symbol,interval,beginDateTime,finishDateTime,limit));
        return "symbol_details";
    }*/

}
