package pl.devzyra.restwebservice.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.devzyra.restwebservice.model.entities.UserEntity;
import pl.devzyra.restwebservice.model.entities.VerificationTokenEntity;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final VerificationTokenService verificationTokenService;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;


    public EmailService(VerificationTokenService verificationTokenService, TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.verificationTokenService = verificationTokenService;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }





    public void sendHtmlMail(UserEntity user) throws MessagingException {
        VerificationTokenEntity verificationToken = verificationTokenService.findByUserEntity(user);

        if(verificationToken != null){
            String token = verificationToken.getToken();
            Context context = new Context();
            context.setVariable("title","Verify your email address");
            context.setVariable("link","http://localhost:8080/activation?token=" + token);

            // passing variables to HTML template
            String body = templateEngine.process("verification",context);

            // Sending msg
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(user.getEmail());
            helper.setSubject("Email address verification");
            helper.setText(body,true);
            javaMailSender.send(message);

        }


    }

}
