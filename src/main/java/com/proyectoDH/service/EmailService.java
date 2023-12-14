package com.proyectoDH.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendConfirmationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de Correo Electrónico");
        message.setText("Haz clic en el siguiente enlace para confirmar tu correo electrónico: "
                + "http://tu-aplicacion.com/confirmar?token=" + token);
        javaMailSender.send(message);
    }
}
