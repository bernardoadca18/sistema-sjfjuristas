package com.sjfjuristas.plataforma.backend.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService
{
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remetente;

    @Async
    public boolean enviarEmail(String para, String assunto, String corpo)
    {
        logger.info("Tentando enviar e-mail para: {}", para);

        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(corpo);
            emailSender.send(message);

            logger.info("E-mail enviado com sucesso!");
            return true;
        }
        catch(MailException e)
        {
            logger.error("Falha ao enviar e-mail para {}: {}", para, e.getMessage());
            return false;
        }
    }

    @Async
    public void enviarEmailHtml(String para, String assunto, String templateNome, Map<String, Object> variaveis) {
        logger.info("Iniciando preparação do e-mail HTML para: {}", para);

        try
        {
            Context context = new Context();
            context.setVariables(variaveis);

            String corpoHtml = templateEngine.process(templateNome, context);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpoHtml, true);

            emailSender.send(mimeMessage);
            logger.info("E-mail HTML enviado com sucesso para: {}", para);
        } 
        catch (MessagingException e)
        {
            logger.error("Falha ao enviar e-mail HTML para {}: {}", para, e.getMessage(), e);
        }
    }
}
