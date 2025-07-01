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
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String USERNAME = "Seu email aqui"; // SEU E-MAIL
    private static final String PASSWORD = "Senha de app aqui";   // SUA SENHA DE APP

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

            // CORREÇÃO: Usar BCC (Cópia Oculta) para proteger a privacidade dos destinatários.
            // O e-mail é enviado para o próprio remetente e os outros ficam em cópia oculta.
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(USERNAME));
            for (String email : destinatarios) {
                // Adiciona cada e-mail cadastrado como um destinatário em cópia oculta.
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email));
            }

            message.setSubject("[ALERTA] Guardou-Pagou: Fatura(s) com vencimento próximo");
            message.setText(construirCorpoEmail(faturas));

            Transport.send(message);
            LOGGER.info("E-mail de alerta enviado com sucesso para: " + String.join(", ", destinatarios));

        } catch (AuthenticationFailedException authEx) {
            LOGGER.log(Level.SEVERE, "Falha na autenticação do e-mail. Verifique se o USERNAME e a SENHA DE APP estão corretos em EmailService.java.", authEx);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Falha ao enviar e-mail de alerta. Verifique a conexão com a internet e as configurações de SMTP.", e);
        }
    }

    private String construirCorpoEmail(List<Fatura> faturas) {
        StringBuilder sb = new StringBuilder();
        sb.append("Olá!\n\nAs seguintes faturas estão próximas do vencimento:\n\n");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (Fatura fatura : faturas) {
            sb.append(String.format("- NF %s | Fornecedor: %s | Vencimento: %s | Valor: %s\n",
                    fatura.getNumeroNota(),
                    fatura.getMarca(), // Supondo que 'getMarca' retorne o fornecedor
                    fatura.getVencimento().format(dateFormatter),
                    currencyFormatter.format(fatura.getValor())
            ));
        }

        sb.append("\nPor favor, verifique o sistema para realizar o pagamento dentro do prazo.\n\n");
        sb.append("---\n");
        sb.append("Esta é uma mensagem automática do sistema Guardou-Pagou. Favor não responder.\n");

        return sb.toString();
    }
}