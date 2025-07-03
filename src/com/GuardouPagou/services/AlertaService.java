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
        System.out.println("[AlertaService] Agendador iniciado: executando a cada 24h");
    }

    public void parar() {
        System.out.println("[AlertaService] Parando agendador de alertas...");
        scheduler.shutdownNow();
        System.out.println("[AlertaService] Agendador parado.");
    }

    private void verificarEEnviar() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(3);
        System.out.println("[AlertaService] -----------------------------");
        System.out.println("[AlertaService] Início de verificação em: " + hoje);
        System.out.println("[AlertaService] Buscando faturas pendentes até: " + limite);

        try {
            List<Fatura> faturas = faturaDAO.buscarFaturasPendentesAte(limite);
            System.out.println("[AlertaService] Faturas encontradas: " + faturas.size());

            if (faturas.isEmpty()) {
                System.out.println("[AlertaService] Nenhuma fatura vencendo em até 3 dias. Nada a enviar.");
                return;
            }

            List<AlertaEmail> emails = emailDAO.listarEmails();
            System.out.println("[AlertaService] E-mails de alerta cadastrados: " + emails.size());

            for (AlertaEmail dest : emails) {
                String email = dest.getEmail();
                System.out.println("[AlertaService] Enviando alerta para: " + email);
                EmailSender.enviarAlerta(email, faturas);
                System.out.println("[AlertaService] Alerta enviado com sucesso para: " + email);
            }

        } catch (Exception e) {
            System.err.println("[AlertaService] ERRO ao verificar/enviar alertas:");
            e.printStackTrace();
        } finally {
            System.out.println("[AlertaService] Fim da verificação diária.");
        }
    }
}
