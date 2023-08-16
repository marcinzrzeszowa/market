package com.mj.market.market.chart;

import com.mj.market.market.MarketService;
import com.mj.market.market.dto.ResponseDto;
import org.hibernate.annotations.Comment;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class MarketChart {

    //TODO
    private final MarketService marketService;
    private String chart;


    @Autowired
    public MarketChart(MarketService marketService) {
        this.marketService = marketService;
    }

    public String getChart() {
        if(chart != null) return chart;
        return "Empty chart";
    }

    public List<ResponseDto> getPriceDetails(String symbol, String interval, String beginDateTime, String finishDateTime, String limit) {
        List<ResponseDto> priceDetails = marketService.getPriceDetails(symbol, interval, beginDateTime, finishDateTime, limit);
        DefaultHighLowDataset dataset = createDataSet(priceDetails);
        JFreeChart chart = createChart(dataset);
        this.chart= convertChartToBase64(chart);
        return priceDetails;
    }

    private JFreeChart createChart(DefaultHighLowDataset dataset) {
        return ChartFactory.createCandlestickChart("Candlestick Chart", "Date", "Value", dataset, false);
    }


    private DefaultHighLowDataset createDataSet(List<ResponseDto> list){
        int size = list.size();
        Date[] date = new Date[size];
        double[] high = new double[size];
        double[] low = new double[size];
        double[] open = new double[size];
        double[] close = new double[size];
        double[] volume = new double[size];

        String name = "";

        for (int i = 0; i < size; i++) {
            ResponseDto obj = list.get(i);
            Date objDate = Date.from(obj.getCloseTime().atZone(ZoneId.systemDefault()).toInstant());
            date[i] = objDate;
            high[i] = obj.getHighPrice().doubleValue();
            low[i] = obj.getLowPrice().doubleValue();
            open[i] = obj.getOpenPrice().doubleValue();
            close[i] = obj.getClosePrice().doubleValue();
            volume[i] = obj.getVolume().doubleValue();
        }
        return new DefaultHighLowDataset(name, date, high, low, open, close, volume);
    }

    private String convertChartToBase64(JFreeChart chart) {
        byte[] chartBytes = new byte[0];
        try {
            chartBytes = ChartUtils.encodeAsPNG(chart.createBufferedImage(600, 300));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(chartBytes);
    }



}
