package com.mj.market.market.chart;

import com.mj.market.market.*;
import com.mj.market.market.BinanceSymbol;
import com.mj.market.market.dto.ResponseDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Chart extends JFrame {
    private  BinanceMarketApi market = new BinanceMarketApi();

    public static void main(String[] args) {

        Chart simpleChart = new Chart();
    }


    public Chart(){
        XYDataset dataset = null;
        try {
            dataset = createDataSet();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(chartPanel);
        pack();
        setTitle("Wykres");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private XYDataset createDataSet() throws ParseException {
        BinanceSymbol symbol = BinanceSymbol.valueOf("ETHUSDT");
        Interval interval = Interval.valueOfLabel("1d");
        LocalDateTime startDate = DateTime.toLocalDateTime("2022-04-28 07:40:52");
        LocalDateTime endDate = DateTime.toLocalDateTime("2023-06-29 07:40:52");
        int limit = Integer.valueOf("1000".trim());

        List<ResponseDto> detailPriceHistory = market.getDetailPriceHistory(symbol, interval, startDate, endDate, limit);

        String time = "Interwał: "+ interval.toString();
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries(time);


        for(ResponseDto obj: detailPriceHistory){

            LocalDateTime objTime= obj.getCloseTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(objTime.toString());
            series.add(new Day(date),obj.getClosePrice());
        }
        dataSet.addSeries(series);
        return dataSet;
    }

    private static JFreeChart createChart(XYDataset dataset){
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Ceny krypto",
                "Dni",
                "Cena",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.black);
        return chart;
    }

}
