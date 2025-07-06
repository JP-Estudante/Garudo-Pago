package com.GuardouPagou.services;

import com.GuardouPagou.models.Fatura;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class EmailSender {

    private static final Properties config = new Properties();
    private static final String USER;
    private static final String PASS;
    private static final String HOST;
    private static final String PORT;
    private static final boolean AUTH;
    private static final boolean STARTTLS;

    static {
        try (InputStream in = EmailSender.class
                .getClassLoader()
                .getResourceAsStream("properties/email.properties")) {
            if (in == null) {
                throw new RuntimeException("Não foi possível encontrar email.properties");
            }
            config.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        USER     = config.getProperty("mail.user");
        PASS     = config.getProperty("mail.pass");
        HOST     = config.getProperty("mail.host");
        PORT     = config.getProperty("mail.port", "587");
        AUTH     = Boolean.parseBoolean(config.getProperty("mail.auth", "true"));
        STARTTLS = Boolean.parseBoolean(config.getProperty("mail.starttls", "true"));
    }

    public static void enviarAlerta(String destinatario, List<Fatura> faturas) throws MessagingException {
        if (USER == null || PASS == null || HOST == null) return;

        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(AUTH));
        props.put("mail.smtp.starttls.enable", String.valueOf(STARTTLS));
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("[ALERTA] Fatura prestes a vencer");

        StringBuilder sb = new StringBuilder();
        sb.append("Faturas vencendo nos próximos dias:\n\n");
        for (Fatura f : faturas) {
            sb.append(String.format("NF: %s | Marca: %s | Venc: %s | Valor: R$ %.2f\n",
                    f.getNumeroNota(), f.getMarca(), f.getVencimento(), f.getValor()));
        }
        message.setText(sb.toString());

        Transport.send(message);
    }
}
