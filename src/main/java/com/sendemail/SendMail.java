package com.sendemail;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.Importance;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class SendMail {
    private static final String recipients = "письма";
    private static final String URI = "https://mail.at-consulting.ru/ews/Exchange.asmx";
    private static final ExchangeVersion exchangeVersion = ExchangeVersion.Exchange2010;
    private static final String domain = "at-consulting";
    private static final String username = "mbigvava";
    private static final String password = "sailor03079422062020Dm";
    private static final String subject = "iSpring в Казахстане — Решение для быстрого обучения сотрудников и студентов";

    public static void main(String[] args) {
        String[] separatedRecipients = recipients.split("\n");
        List<String> listEmails = Arrays.asList(separatedRecipients);

        ExchangeService exchangeService = new ExchangeService(exchangeVersion);
        ExchangeCredentials exchangeCredentials = new WebCredentials(username, password, domain);
        exchangeService.setCredentials(exchangeCredentials);

        try {
            exchangeService.setUrl(new URI(URI));
        } catch (URISyntaxException ex) {
            System.out.println("An exception occured while creating the uri for exchange service." + ex);
            return;
        }

        for (String email : listEmails) {

            EmailMessage emailMessage;
            try {
                emailMessage = new EmailMessage(exchangeService);
                emailMessage.setSubject(subject);
                emailMessage.setImportance(Importance.High);

                String html = "<html>" +
                        "<head>" +
                        "</head>" +
                        "<body>" +
                            "<img src=\"cid:header.png\">" +
                            "<big><p> Добрый день! </p>" +
                            "<p> Благодарим за интерес к iSpring!</p>" +
                            "<p> Наша компания ТОО «Эй Ти Си Каз» – официальные партнеры «iSpring» в Казахстане. <br>" +
                            "Планируете использовать программы iSpring в дальнейшем для реализации ваших проектов <br> " +
                            "и задач в компании? Готовы проконсультировать Вас и обсудить сотрудничество!</p>" +
                            "<p> Возникли вопросы? Пожалуйста, обращайтесь! Буду рада помочь!</p>" +
                            "<p> С уважением,</p>" +
                            "<p> Мариам Бигвава <br>" +
                            "Аналитик ТОО «Эй Ти Си Каз» | «Лига Цифровой Экономики» </p></big>" +
                            "<p><img src=\"cid:mailFooter.png\"> <big>mbigvava@at-consulting.ru | http://www.digitalleague.ru</big></p>" +
                            "<p><img src=\"cid:phoneFooter.png\"> <big>T.: +7 (777) 715 70 14</big></p>" +
                        "</body>" +
                        "</html>";
                emailMessage.setBody(new MessageBody(BodyType.HTML, html));

                String header = "src/main/resources/header.png";
                emailMessage.getAttachments().addFileAttachment("header.png", header);
                emailMessage.getAttachments().getItems().get(0).setIsInline(true);
                emailMessage.getAttachments().getItems().get(0).setContentId("header.png");

                String mailFooter = "src/main/resources/mailFooter.png";
                emailMessage.getAttachments().addFileAttachment("mailFooter.png", mailFooter);
                emailMessage.getAttachments().getItems().get(1).setIsInline(true);
                emailMessage.getAttachments().getItems().get(1).setContentId("mailFooter.png");

                String phoneFooter = "src/main/resources/phoneFooter.png";
                emailMessage.getAttachments().addFileAttachment("phoneFooter.png", phoneFooter);
                emailMessage.getAttachments().getItems().get(2).setIsInline(true);
                emailMessage.getAttachments().getItems().get(2).setContentId("phoneFooter.png");

                try {
                    emailMessage.getToRecipients().add(email);
                } catch (ServiceLocalException ex) {
                    System.out.println("An exception occured while setting the TO recipient(" + email + ")." + ex.getMessage());
                    return;
                }

                try {
                    emailMessage.sendAndSaveCopy();
                    System.out.println("An email is send to: " + email);
                } catch (Exception ex) {
                    System.out.println("An exception occured while sending and saving an email." + ex.getMessage());
                }
            } catch (Exception ex) {
                System.out.println("An exception occured while creating the email message." + ex.getMessage());
            }
        }
    }
}