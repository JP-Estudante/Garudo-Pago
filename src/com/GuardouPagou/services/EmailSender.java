package com.GuardouPagou.services;

import com.GuardouPagou.models.Fatura;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import javax.xml.transform.Source;

public class EmailSender {

    //Usando Intellij
//    private static final String USER = System.getenv("MAIL_USER");
//    private static final String PASS = System.getenv("MAIL_PASS");
//    private static final String HOST = System.getenv("MAIL_HOST");

    //Usando NetBeans
    //private static final String USER = System.getProperty("MAIL_USER");
    //private static final String PASS = System.getProperty("MAIL_PASS");
    //private static final String HOST = System.getProperty("MAIL_HOST");

    public static void enviarAlerta(String destinatario, List<Fatura> faturas) throws MessagingException {
        if (USER == null || PASS == null || HOST == null) return;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587");

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
