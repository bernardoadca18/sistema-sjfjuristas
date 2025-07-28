package com.sjfjuristas.plataforma.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

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
}
