package com.mj.market.config;

import com.mj.market.app.article.Article;
import com.mj.market.app.article.ArticleService;
import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.pricealert.PriceAlertService;
import com.mj.market.app.symbol.Symbol;
import com.mj.market.app.symbol.SymbolService;
import com.mj.market.app.symbol.SymbolType;

import com.mj.market.app.user.Role;
import com.mj.market.app.user.User;
import com.mj.market.app.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;


@Component
@AllArgsConstructor
class StartupData implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ArticleService articleService;
    private final SymbolService symbolService;
    private final PriceAlertService priceAlertService;

    @Override
    public void run(String... args) throws Exception {
          loadArticles();
          Load();
    }

    private void Load(){
        User admin = User.builder()
                .username("admin")
                .email("marcinzbrzozowa@gmail.com")
                .role(Role.ROLE_ADMIN)
                .locked(false)
                .enabled(true)
                .password(passwordEncoder.encode("123"))
                .build();
        User user1 = User.builder()
                .username("test")
                .email("marcinzbrzozowa@gmail.com")
                .role(Role.ROLE_USER)
                .locked(false)
                .enabled(true)
                .password(passwordEncoder.encode("test"))
                .build();
        userService.saveUser(admin);
        userService.saveUser(user1);


        Symbol btcUsdt = new Symbol("Bitcoin / USDT","BTCUSDT", SymbolType.KRYPTOWALUTA);
        Symbol ethUsdt  = new Symbol("Ethereum / USDT","ETHUSDT", SymbolType.KRYPTOWALUTA);
        Symbol bnbUsdt = new Symbol("Binance USD / USDT","BNBUSDT", SymbolType.KRYPTOWALUTA);
        Symbol maticUsdt = new Symbol("Matic / USDT","MATICUSDT", SymbolType.KRYPTOWALUTA);

        Symbol tether = new Symbol("USD / Tether USDT", "USDTTRY", SymbolType.KRYPTOWALUTA);
        Symbol solana = new Symbol("Solana / Binance USD","SOLBUSD", SymbolType.KRYPTOWALUTA);
        Symbol cardano = new Symbol("Cardano / USDT","ADAUSDT", SymbolType.KRYPTOWALUTA);
        Symbol zilliqa = new Symbol("Zilliqa / USDT","ZILUSDT", SymbolType.KRYPTOWALUTA);
        Symbol polkadot = new Symbol("Polkadot / USDT","DOTUSDT", SymbolType.KRYPTOWALUTA);
        Symbol xrp = new Symbol("Ripple / USDT","XRPUSDT", SymbolType.KRYPTOWALUTA);
        Symbol cosmos = new Symbol("Atom / USDT","ATOMUSDT", SymbolType.KRYPTOWALUTA);

        Symbol eurPln = new Symbol("EUR / PLN","EURPLN", SymbolType.WALUTA);
        Symbol usdPln = new Symbol("USD / PLN","USDPLN", SymbolType.WALUTA);
        Symbol eurUsd = new Symbol("EUR / USD","EURUSD", SymbolType.WALUTA);
        Symbol chfPln = new Symbol("CHF / PLN","CHFPLN", SymbolType.WALUTA);

        Symbol silverUsd = new Symbol("SREBRO / USD","SREBRO", SymbolType.SUROWIEC);

        symbolService.saveAllSymbols(Arrays.asList(btcUsdt,ethUsdt,bnbUsdt,maticUsdt,eurPln,usdPln,eurUsd,chfPln,silverUsd,tether,solana,cardano,zilliqa,polkadot, xrp, cosmos));

        PriceAlert pa1 = PriceAlert.newPriceAlertWithMaxMin(btcUsdt, admin,"Cena BTC poza zakresem cenowym", new BigDecimal(23000), new BigDecimal(16000));
        PriceAlert pa4 = PriceAlert.newPriceAlertWithMax(ethUsdt,admin,"ETH below 20.000", new BigDecimal(20000));
        PriceAlert pa2 = PriceAlert.newPriceAlertWithMin(btcUsdt,admin,"Cena BTC spadła !", new BigDecimal(16000));

        PriceAlert pa3 = PriceAlert.newPriceAlertWithMax(usdPln,user1,"Cena USD drastycznie wzrosła ! ",new BigDecimal(4.23));
        PriceAlert pa5 = PriceAlert.newPriceAlertWithMin(eurPln, user1,"EUR spada", new BigDecimal(4.63));
        PriceAlert pa6 = PriceAlert.newPriceAlertWithMaxMin(eurPln, user1,"EUR poza zakresem",  new BigDecimal(4.5),  new BigDecimal(4));

        priceAlertService.savePriceAlerts(Arrays.asList(pa1,pa2,pa3,pa4,pa5,pa6));

    }
    //&nbsp;
    private void loadArticles(){
        Article a1 = new Article(
                "Witam.<br/>Zakładka \"Notowania giełdowe\" zawiera odnośnik do aktualnych cen z giełdy. Proszę o cierpliwość przy ładowaniu ponieważ dane pochodzą z darmowego źródła. " +
                        "Treść strony zostanie porzeżona o&nbsp;inną tematykę i nowe funkcjonalności.",
                "Szukałem tematu na ciekawy projekt programistyczny, i stąd pomysł na stworzenie strony z&nbsp;kursami giełdowymi. " +
                        " <br/> Projekt powstał z wykorzystaniem języka programowania Java, oraz technologii: Spring Boot, MySQL, Thymeleaf, CSS, HTML. <br/> " +
                "<br/>Główną funkcjonalnością mojej aplikacji jest:</br>" +
                "<ul>" +
                "   <li>" +
                        "Pobieranie aktualnego kursów surowców, indeksów, akcji, walut i kryptowalut z giełdy. Wykożystałem do tego dane, udostępniane z zewnętrznej strony. " +
                        "Serwis daje dostęp do darmowego interfejs dla programistów, i nie nakłada dużych ograniczeń na programistę w&nbsp;wersji bezpłatnej." +
                        "<br/>Aktualizacja danych zewnętrzych z giełdy odbywa się, w regularnym odstępie czasowym ustawionym na pięć minut. " +
                        "   </li>"+
                "   <li>" +
                        "Kolejną funkcjonalnością jest rejestracja użytkowników. Przechowywanie komunikatów tekstowych od użytkowników w bazie danych. Każdy komunikat odnosi się waloru z&nbsp;giełdy. " +
                        "Komunikaty mają na celu informować użutkownika o zmianie ceny rynkowaj w&nbsp;interesującym go zakresie. "+

                "   </li>"+
                "   <li>" +
                "Ceny z giełdy porównywane są z zakresem cen zapisanym przez użytkowników." +
                "<br/>Jeżeli aktulnay kurs z giełdy przkracza wartość ceny maksymalnej, lub spada poniżej wartości ceny minimalnej, zostanie wysłana wiadomość. " +
                "Każda wiadomość powiązana jest z kontem użytkownika i zotaje przesłana na wskazany przy rejestracji adres e-mail. " +
                "Następnie wiadomość przetaje być aktywna i nie jest brana pod uwagę przy kolejnym cyklu sprawdzania ceny. " +
                    "<li>" +
                        "Aplikacja nasłuchuje zmian w liscie wiadomości uzytkowników w celu jej aktualizacji. " +
                     "</li>"+
                        "<li>" +
                        "Tylko zarejestrowany użytkownik ma możliwość dodawania i edycji wiadomości z&nbsp;powiadomieniami. " +
                        "</li>"+
                        "<li>" +
                        "Konto administratora umożliwia zarządzanie użytkownikami i wiadomościami wszystkich użytkowników. " +
                        "</li>"+
                        "<li>" +
                        "Każdy odwiedzający stronę, ma możliwość przeglądnięcia kursów giełdowych z dostępnej listy wraz z&nbsp;wskaźnikami giełdowymi oraz zapoznaniem się z nowościami na stronie startowej. " +
                        "</li>"+
                        "" +
                        "<li>" +
                        "<a href=\"https://github.com/marcinzrzeszowa/project0\">Kod źródłowy</a> dostępny na github.com" +
                        "</li>"+
                        "" +
                        "</div>" +
                "   </li>"+
                "</ul>");
        articleService.saveArticles(Arrays.asList(a1));
    }
}
