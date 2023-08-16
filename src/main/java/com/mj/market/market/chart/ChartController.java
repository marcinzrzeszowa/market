package com.mj.market.market.chart;

import com.mj.market.market.MarketService;
import com.mj.market.market.dto.ResponseDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;


public class ChartController {

    private final MarketService marketService;


    public ChartController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/prices/{symbol}")
    public String getPriceDetails(
            @PathVariable String symbol,
            @RequestParam String interval,
            @RequestParam String beginDateTime,
            @RequestParam String finishDateTime,
            @RequestParam String limit,
            Model model){

        List<ResponseDto> priceDetails = marketService.getPriceDetails(symbol, interval, beginDateTime, finishDateTime, limit);

        // Generate charts
        for (ResponseDto obj : priceDetails) {
            JFreeChart chart = generateCandlestickChart(obj);
            String base64EncodedChart = convertToBase64(chart);
            obj.setChart(base64EncodedChart);
        }
        model.addAttribute("candlestickData", priceDetails);
        return "view";
    }

    private JFreeChart generateCandlestickChart(ResponseDto data) {

        String name = "";
        Date date = Date.from(data.getCloseTime().atZone(ZoneId.systemDefault()).toInstant());
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(
                name ,
                new Date[]{date},
                new double[]{data.getHighPrice().doubleValue()},
                new double[]{data.getLowPrice().doubleValue()},
                new double[]{data.getOpenPrice().doubleValue()},
                new double[]{data.getClosePrice().doubleValue()},
                new double[]{data.getVolume().doubleValue()}
        );

        return ChartFactory.createCandlestickChart("Candlestick Chart", "Date", "Value", dataset, false);
    }

    private String convertToBase64(JFreeChart chart) {
        byte[] chartBytes = new byte[0];
        try {
            chartBytes = ChartUtils.encodeAsPNG(chart.createBufferedImage(400, 300));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(chartBytes);
    }
}
