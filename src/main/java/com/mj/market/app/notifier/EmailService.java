package com.mj.market.app.notifier;

import com.mj.market.app.pricealert.PriceAlert;
import com.mj.market.app.user.User;
import com.mj.market.app.user.registration.RegistrationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


@Service
@Primary
@Qualifier("EmailNotifier")
public class EmailService implements UserAlertNotifier, RegistrationTokenSender {

    private final JavaMailSender mailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Value("${notifier.price-alert.on}")
    private boolean USER_ALERTS_NOTIFICATION_ON;
    @Value("${notifier.price-alert.email-from}")
    private String SEND_FROM_EMAIL;
    @Value("${notifier.price-alert.email-replay-to}")
    private String SEND_REPLY_TO_EMAIL;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    private record Message(String email, String titleMsg, String message) {
    }

    @Async
    @Override
    public void notifyUserAboutPriceChange(Set<PriceAlert> priceAlert) {
        for (PriceAlert pa : priceAlert) {
            Message msg = createNotifyMessage(pa);
            MimeMessage message = createEmail(msg.email(), msg.titleMsg(), msg.message());
            if(USER_ALERTS_NOTIFICATION_ON) sendEmail(message);
        }
    }

    private static Message createNotifyMessage(PriceAlert priceAlert) {
        long id = priceAlert.getId();
        String userName = priceAlert.getUser().getUsername();
        String email = priceAlert.getUser().getEmail();
        String description = priceAlert.getDescription();
        String ticker = priceAlert.getSymbol().getName();

        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = LocalTime.now().format(simpleDateFormat);

        String titleMsg = "Zmiana ceny | Kurs: " + ticker;
        StringBuilder subjectMsg = new StringBuilder();
        subjectMsg.append("Czas: ").append(time);
        String message = "<div>" +
                "<h2>Alert cenowy Nr: " + id + " wysłany z strony projectarea.pl</h2>" +
                "<h3> Cześć " + userName + "</h3>" +
                "<p> Wiadomość: " + subjectMsg + "</p>" +
                "<p>Twoja notatka: " + description + "</p>" +
                "</div>";
        Message result = new Message(email, titleMsg, message);
        return result;
    }

    @Override
    public void sendRegistrationToken(RegistrationToken token) {
        Message msg = createRegistrationTokenMessage(token);
        MimeMessage mimeMessage = createEmail(msg.email(), msg.titleMsg(), msg.message());
        sendEmail(mimeMessage);
    }

    private static Message createRegistrationTokenMessage(RegistrationToken token) {
        String userName = token.getUser().getUsername();
        String email = token.getUser().getEmail();

        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("dd:MM:yyyy");
        String time = token.getExpiresAt().format(simpleDateFormat);

        String titleMsg = "Rejestracja na stronie http://projectarea.alwaysdata.net/";
        String message = "<div>" +
                "<h1>Potwierdź rejestrację</h1>" +
                "<h2> Cześć " + userName + "</h2>" +
                "<p>Potwierdź link aktywacyjny konto do dnia: " + time + " ,po którym nastąpi jego dysaktywacja</p>" +
                "<p> <a href=\"http://localhost:8080/register/confirm/" + token.getToken() + "\">AKTYWUJ KONTO</a></p>" +
                "</div>";
        Message result = new Message(email, titleMsg, message);
        return result;
    }

    private void sendEmail(MimeMessage message) {
        mailSender.send(message);
    }

    private MimeMessage createEmail(String email, String titleMsg, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(email);
            helper.setSubject(titleMsg);
            helper.setFrom(SEND_FROM_EMAIL);
            helper.setReplyTo(SEND_REPLY_TO_EMAIL);
            return mimeMessage;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}