package com.mj.market.app.market;

import com.mj.market.app.chart.MarketChart;
import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//@RestController
@Controller
public class MarketController {

    private final MarketService marketService;
    private final MarketChart marketChart;

    @Autowired
    public MarketController(MarketService marketService, MarketChart marketChart) {
        this.marketService = marketService;
        this.marketChart = marketChart;
    }


    @GetMapping(value = {"/symbols"})
    public String getSymbolsPrice(Model model){
        model.addAttribute("symbols",  marketService.getAllPrices());
        return "symbols";
    }

    @GetMapping("/symbols/{code}")
    public String getPriceDetails(
            @PathVariable String symbol,
            @RequestParam String interval,
            @RequestParam String beginDateTime,
            @RequestParam String finishDateTime,
            @RequestParam String limit, Model model){
        model.addAttribute("symbol",  marketService.getPriceDetails(symbol,interval,beginDateTime,finishDateTime,limit));
        return "symbolDetails";
    }
/*

    //todo param
    @GetMapping("/priceChart/{symbol}")
    public String getPriceChartDetails(
            @PathVariable String symbol,
            @RequestParam String interval,
            @RequestParam String beginDateTime,
            @RequestParam String finishDateTime,
            @RequestParam String limit,
            Model model){

        List<ResponseDto> responseList =  marketChart.getPriceDetails(symbol,interval,beginDateTime,finishDateTime,limit);
        String chart = marketChart.getChart();
        model.addAttribute("responseDtoList", responseList);
        model.addAttribute("responseChart", chart);
        return "view2";
    }
*/

}
