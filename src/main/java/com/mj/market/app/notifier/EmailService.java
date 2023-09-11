package com.mj.market.app.notifier;

import com.mj.market.app.pricealert.PriceAlert;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


@Service
public class EmailService implements UserNotifier {

    private final JavaMailSender mailSender;
    private static final Logger LOGGER= LoggerFactory.getLogger(EmailService.class);
    private static final String SEND_FROM_EMAIL = "projectarea@onet.pl";
    private static final String SEND_REPLY_TO_EMAIL = "projectarea@onet.pl";
    public static final boolean SEND = false;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    private record Message(String userName, String email, String titleMsg, String message) {
    }

    @Async
    @Override
    public void notify(Set<PriceAlert> priceAlert){
        for (PriceAlert pa: priceAlert){
            Message msg = getMessage(pa);
            sendEmail(msg.userName(), msg.email(), msg.titleMsg(), msg.message());
        }
    }

    private static Message getMessage(PriceAlert priceAlert) {
        long id = priceAlert.getId();
        String userName = priceAlert.getUser().getUsername();
        String email = priceAlert.getUser().getEmail();
        String description = priceAlert.getDescription();
        String ticker = priceAlert.getSymbol().getName();

        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = LocalTime.now().format(simpleDateFormat);

        String titleMsg ="Zmiana ceny | Kurs: "+ ticker;
        StringBuilder subjectMsg= new StringBuilder();
        subjectMsg.append("Czas: ").append(time);
        String message ="<div>" +
                        "<h1>Alert cenowy Nr: "+ id +" wysłany z strony projectarea.pl</h1>" +
                        "<h2> Cześć "+ userName + "</h2>" +
                        "<p> Wiadomość: "+ subjectMsg +"</p>" +
                        "<p>Twoja notatka: "+ description +"</p>" +
                        "</div>";
        Message result = new Message(userName, email, titleMsg, message);
        return result;
    }


    private void sendEmail(String userName, String email, String titleMsg, String message) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message,true);
            helper.setTo(email);
            helper.setSubject(titleMsg);
            helper.setFrom(SEND_FROM_EMAIL);
            helper.setReplyTo(SEND_REPLY_TO_EMAIL);
            if(SEND) mailSender.send(mimeMessage);
        }catch(MessagingException e){
            LOGGER.info("Nie można wysłać wiadomości pod adress: " + email + " uzytkownika: "+ userName, e);
            e.printStackTrace();
        }
    }
}
