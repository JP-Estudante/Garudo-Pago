package com.GuardouPagou.services;

import com.GuardouPagou.dao.AlertaEmailDAO;
import com.GuardouPagou.dao.FaturaDAO;
import com.GuardouPagou.models.AlertaEmail;
import com.GuardouPagou.models.Fatura;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlertaService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final AlertaEmailDAO emailDAO = new AlertaEmailDAO();
    private final FaturaDAO faturaDAO = new FaturaDAO();

    public void iniciar() {
        scheduler.scheduleAtFixedRate(this::verificarEEnviar, 0, 1, TimeUnit.DAYS);
    }

    public void parar() {
        scheduler.shutdownNow();
    }

    private void verificarEEnviar() {
        LocalDate limite = LocalDate.now().plusDays(3);
        try {
            List<Fatura> faturas = faturaDAO.buscarFaturasPendentesAte(limite);
            if (faturas.isEmpty()) return;
            List<AlertaEmail> emails = emailDAO.listarEmails();
            for (AlertaEmail dest : emails) {
                EmailSender.enviarAlerta(dest.getEmail(), faturas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
