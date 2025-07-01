package com.GuardouPagou.services;

import com.GuardouPagou.dao.EmailDAO;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.models.Fatura;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VerificadorFaturasAgendado implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(VerificadorFaturasAgendado.class.getName());
    private final FaturaDAO faturaDAO;
    private final EmailDAO emailDAO;
    private final EmailService emailService;

    public VerificadorFaturasAgendado() {
        this.faturaDAO = new FaturaDAO();
        this.emailDAO = new EmailDAO();
        this.emailService = new EmailService();
    }

    /**
     * Método principal que será executado pela tarefa agendada.
     */
    @Override
    public void run() {
        LOGGER.info("Iniciando verificação diária de faturas a vencer...");
        try {
            // Busca faturas vencendo nos próximos 3 dias
            List<Fatura> faturasProximas = faturaDAO.listarFaturasProximasDoVencimento(3);

            if (faturasProximas.isEmpty()) {
                LOGGER.info("Nenhuma fatura encontrada com vencimento nos próximos 3 dias.");
                return;
            }

            // Busca os e-mails cadastrados para receber o alerta
            ObservableList<String> emailsDeAlerta = emailDAO.listarEmails();

            if (emailsDeAlerta.isEmpty()) {
                LOGGER.warning("Faturas a vencer encontradas, mas não há e-mails de alerta cadastrados.");
                return;
            }

            // Envia o e-mail
            emailService.enviarAlertaDeVencimento(emailsDeAlerta, faturasProximas);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro durante a execução da verificação agendada de faturas.", e);
        }
    }

    /**
     * Inicia o agendador para executar a verificação uma vez por dia.
     */
    public void iniciar() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Executa a tarefa agora e depois a cada 24 horas.
        scheduler.scheduleAtFixedRate(this, 0, 24, TimeUnit.HOURS);
        LOGGER.info("Agendador de verificação de faturas iniciado. Próxima execução em 24 horas.");
    }
}