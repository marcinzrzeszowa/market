package com.mj.market.app.notifier;

import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.config.ColorConsole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
@Qualifier("ConsoleNotifier")
public class ConsoleNotifier implements UserNotifier {

    @Override
    public void notify(Set<PriceAlert> priceAlert) {
        for (PriceAlert pa: priceAlert){

            String userName = pa.getUser().getUsername();
            String email = pa.getUser().getEmail();
            String description = pa.getDescription();
            String ticker = pa.getSymbol().getName();

            DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = LocalTime.now().format(simpleDateFormat);

            String titleMsg ="Zmiana ceny | Kurs: "+ ticker +" ";
            StringBuilder subjectMsg= new StringBuilder();
            subjectMsg.append("Czas: ").append(time);
            String message =
                    titleMsg +
                    "Alert cenowy wysłany z strony projectarea.pl \n" +
                    "Cześć "+ userName + " \n" +
                    "Wiadomość: "+ subjectMsg +" \n" +
                    "Twoja notatka: "+ description +" \n";
            ColorConsole.printlnCyan(message);
        }
    }
}
