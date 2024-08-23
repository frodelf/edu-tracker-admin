package ua.kpi.edutrackeradmin.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.service.EmailService;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${sendgrid.api.key}")
    private String apiKey;
    @Value("${sendgrid.api.from}")
    private String fromEmail;

    @Override
    public void sendEmail(String subject, String text, String toEmail) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            int status = response.getStatusCode();
            System.out.println(status);
        } catch (IOException ex) {
        }
    }
}