package com.GuardouPagou.services;

import com.GuardouPagou.models.Fatura;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    // --- CONFIGURAÇÕES DO SERVIDOR DE E-MAIL (SMTP) ---
    // IMPORTANTE: Substitua pelos dados do seu servidor de e-mail.
    // Exemplo para o Gmail. Se usar, lembre-se de criar uma "Senha de App".
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String USERNAME = "gabrielberleifrs@gmail.com"; // SEU E-MAIL
    private static final String PASSWORD = "54GBsellback?";   // SUA SENHA DE APP

    private final Session session;

    public EmailService() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    public void enviarAlertaDeVencimento(List<String> destinatarios, List<Fatura> faturas) {
        if (destinatarios.isEmpty() || faturas.isEmpty()) {
            LOGGER.info("Nenhum destinatário ou fatura para enviar alerta. Abortando.");
            return;
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));

            // Adiciona todos os destinatários
            for (String email : destinatarios) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }

            message.setSubject("[ALERTA] Fatura(s) prestes a vencer");
            message.setText(construirCorpoEmail(faturas));

            Transport.send(message);
            LOGGER.info("E-mail de alerta enviado com sucesso para: " + String.join(", ", destinatarios));

        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Falha ao enviar e-mail de alerta.", e);
        }
    }

    private String construirCorpoEmail(List<Fatura> faturas) {
        StringBuilder sb = new StringBuilder();
        sb.append("Olá! As seguintes faturas estão próximas do vencimento:\n\n");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (Fatura fatura : faturas) {
            sb.append(String.format("- NF %s | Marca: %s | Vencimento: %s | Valor: %s\n",
                    fatura.getNumeroNota(),
                    fatura.getMarca(),
                    fatura.getVencimento().format(dateFormatter),
                    currencyFormatter.format(fatura.getValor())
            ));
        }

        sb.append("\nPor favor, verifique e realize o pagamento dentro do prazo.\n\n");
        sb.append("---\n\n");
        sb.append("Mensagem automática do sistema, favor não responder!\n\n");
        sb.append("Att,\nGuardou-Pagou");

        return sb.toString();
    }
}