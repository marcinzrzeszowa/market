package com.mj.market.app.market.api;


import com.mj.market.app.market.MarketSchedulerSequence;
import com.mj.market.app.market.dto.ResponseDto;
import com.mj.market.app.market.dto.SimpleRequestDto;
import com.mj.market.app.market.dto.SimpleResponseDto;
import com.mj.market.app.notifier.UserNotifier;
import com.mj.market.app.pricealert.PriceAlertService;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;
import com.mj.market.config.ColorConsole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Qualifier("BankierMarketApi")
public class BankierMarketApi extends MarketSchedulerSequence implements MarketApi {


    private static final EnumSet<SymbolType> handledSymbols = EnumSet.of(SymbolType.KRYPTOWALUTA);

    //TODO
    public static final String EURPLN = "https://www.bankier.pl/waluty/kursy-walut/forex/EURPLN";
    public static final String USDPLN ="https://www.bankier.pl/waluty/kursy-walut/forex/USDPLN";
    public static final String EURUSD ="https://www.bankier.pl/waluty/kursy-walut/forex/EURUSD";
    public static final String CHFPLN ="https://www.bankier.pl/waluty/kursy-walut/forex/CHFPLN";
   // public static final String GOLD ="https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=ZLOTO";
    public static final String SILVER ="https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=SREBRO";
    //public static final String COPPER ="https://www.bankier.pl/inwestowanie/profile/quote.html?symbol=MIEDZ";

    private final String GPW="https://data.nasdaq.com/data/WSE-warsaw-stock-exchange-gpw";

    public BankierMarketApi(PriceAlertService priceAlertService, SymbolService symbolService, UserNotifier userNotifier) {
        super("BankierMarketApi", priceAlertService, symbolService, userNotifier);
    }

    public static void main(String[] args) throws IOException {

       /* List<String> urls = List.of(EURPLN,USDPLN,EURUSD,CHFPLN,SILVER);

        List<String> multiResponse = multiRequestHttpGet(urls);

        for (String response: multiResponse){
            String[] result = readResponseHttpContent(response);
            Arrays.stream(result).forEach(e ->ColorConsole.printlnRed(e));
        }
*/

        String response = requestHttpGet(SILVER);
        String[] result = readResponseHttpContent(response);
        Arrays.stream(result).forEach(e -> ColorConsole.printlnRed(e));

    }

    private static String[] readResponseHttpContent(String response) {

        String start = "class=\"boxContent boxTable\">";
        String end = "</div>";
        String contentRaw = substringContent(response, start, end);

        ColorConsole.printlnRandom(contentRaw);

        contentRaw = contentRaw
                .trim()
                .replaceAll("\t","")
                .replaceAll("\n", "")
                .replaceAll(" +", " ");


        RawAttribute[] array = new RawAttribute[3];
        array[0] =  new RawAttribute("Symbol", "class=\"small-title\">","</h1>");
        array[1] =  new RawAttribute("Price", "id=\"referencePrice\" data-value=\"","\"");
        array[2] =  new RawAttribute("Benefit 1R", "Stopa zwrotu 1R:</td> <td class=\"textBold\">","</td>");

        String[] result = substringContent(contentRaw, array);
        return result;
    }

    private static String[] substringContent(String rawStr, RawAttribute[] attributes) {
        String[] results = new String[attributes.length];
        int index = 0;
        for (RawAttribute attribute: attributes){
            int startIndex = rawStr.indexOf(attribute.startReqex) + attribute.startReqex.length();
            int endIndex = rawStr.indexOf(attribute.endRegex, startIndex);
            results[index] = rawStr.substring(startIndex, endIndex);
            index++;
        }
        return results;
    }

    private static String substringContent(String rawStr, String beginRegex, String exdRegex) {
        int startIndex = rawStr.indexOf(beginRegex) + beginRegex.length();
        int endIndex = rawStr.indexOf(exdRegex, startIndex);
        return rawStr.substring(startIndex, endIndex);
    }

    private String findCharacteristicByRegex(String regex, String lineOfText){
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher1 = pattern1.matcher(lineOfText);
        String name="";
        while(matcher1.find())
        {
            name =(matcher1.group());
        }
        return name;
    }

    private static String readUrlData(URLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line ;
        StringBuilder result = new StringBuilder();
        while((line = in.readLine()) != null){
            result.append(line).append("\n");
        }
        in.close();
        return result.toString();
    }

    private static URLConnection requestUrlConnection(String ticker ) throws IOException {
            URLConnection conn;
            URL url = new URL(ticker);
            conn = url.openConnection();
            System.out.println("Connected to URL: "+ url);
            return conn;
    }

    private static List<String> multiRequestHttpGet(List<String> urls) throws IOException{
        List<String> response = new ArrayList<>();
        for (String url: urls){
            response.add(requestHttpGet(url));
        }
        return response;
    }

    private static String requestHttpGet(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            return readUrlData(con);
        } else {
            System.out.println("GET request did not work.");
            return "";
        }
    }

    @Override
    protected Set<SymbolType> setSupportedSymbolType() {
        return handledSymbols;
    }

    @Override
    protected List<SimpleResponseDto> requestPricesForScheduler(Set<Symbol> marketSymbols) {
        return null;
    }

    @Override
    public List<SimpleResponseDto> getAllPrices() {
        return null;
    }

    @Override
    public SimpleRequestDto[] getPrices(Set<String> symbols) {
        return null;
    }

    @Override
    public List<ResponseDto> getDetailPriceHistory(String symbol, Interval interval, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        return null;
    }


    private static class RawAttribute {
        String name;
        String startReqex;
        String endRegex;

        public RawAttribute(String name, String startReqex, String endRegex) {
            this.name = name;
            this.startReqex = startReqex;
            this.endRegex = endRegex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartReqex() {
            return startReqex;
        }

        public void setStartReqex(String startReqex) {
            this.startReqex = startReqex;
        }

        public String getEndRegex() {
            return endRegex;
        }

        public void setEndRegex(String endRegex) {
            this.endRegex = endRegex;
        }
    }

}
