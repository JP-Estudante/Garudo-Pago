
# 📖 Manual do Usuário – Guardou-Pagou

## Sobre o Produto

O **Guardou-Pagou** é um software desenvolvido para ajudar comerciantes e administradores a controlar o pagamento de faturas e notas fiscais, garantindo confiabilidade e automatização de alertas. O sistema foi criado seguindo o método Scrum, sempre priorizando as necessidades dos usuários finais e a melhoria contínua.

---

## Índice

- [Equipe do Projeto](#equipe-do-projeto)
- [Como o Scrum foi Utilizado](#como-o-scrum-foi-utilizado)
- [Fluxo de Uso Típico](#fluxo-de-uso-típico)
- [Funcionalidades do Sistema](#funcionalidades-do-sistema)
    - [Visualizar detalhes de uma Nota Fiscal](#1-visualizar-detalhes-de-uma-nota-fiscal-us-4)
    - [Visualizar faturas pendentes](#2-visualizar-faturas-pendentes-na-tela-inicial-us-2)
    - [Filtrar faturas por período](#3-filtrar-faturas-por-período-us-13)
    - [Filtrar faturas por marca](#4-filtrar-faturas-por-marca-us-14-us-6)
    - [Visualizar notas e faturas arquivadas](#5-visualizar-notas-e-faturas-arquivadas-us-7)
    - [Receber alertas por e-mail](#6-receber-alertas-por-e-mail-de-faturas-a-vencer-us-10-us-9)
- [Checklist Rápido](#checklist-rápido)
- [Melhorias Futuras](#melhorias-futuras)
- [Envio de Feedback](#envio-de-feedback)
- [Histórico de Versões](#histórico-de-versões)

---

## Equipe do Projeto

- **Scrum Master:** Andrwss Aires Vieira  
- **Product Owner:** João Pedro Scheffler  
- **UX/UI:** Anna Laura J.  
- **QA:** Tauane Scapin  
- **Devs:** Gabriel Berle (Tech Lead), Henrique Fredrich, Leonardo B de S

---

## Como o Scrum foi Utilizado

As funcionalidades do Guardou-Pagou foram planejadas e priorizadas com base em **histórias de usuário** (User Stories), discutidas em reuniões de Sprint e validadas pelo Product Owner. O manual evoluiu junto com o sistema, acompanhando cada entrega e feedback das Sprints.

---

## Fluxo de Uso Típico

1. Acesse o sistema para visualizar as faturas pendentes na tela inicial.
2. Utilize filtros por período ou por marca para encontrar rapidamente notas e faturas.
3. Veja detalhes completos das notas fiscais e suas faturas associadas.
4. Marque faturas como "Emitida" para manter a lista sempre atualizada.
5. Receba alertas automáticos por e-mail sobre faturas próximas do vencimento.
6. Consulte notas arquivadas para revisões e auditorias.
7. Gerencie e-mails que receberão notificações.

---

## Funcionalidades do Sistema

### 1. Visualizar detalhes de uma Nota Fiscal (US-4)

Permite consultar todas as informações de uma nota fiscal e suas faturas associadas.

**Como usar:**  
- Na lista principal, clique sobre a nota desejada.
- Uma janela/modal será exibida mostrando:
  - Número da nota, data de emissão, marca (fornecedor)
  - Número de faturas, vencimento, valor, status (Emitida/Não Emitida)
- Clique em **Fechar** ou **Voltar** para retornar à listagem.

**Observação:**  
A visualização é somente leitura, sem possibilidade de edição.

---

### 2. Visualizar faturas pendentes na tela inicial (US-2)

Mostra todas as faturas não emitidas mais próximas do vencimento.

**Como usar:**  
- Ao acessar a tela inicial, veja a lista de faturas pendentes, incluindo:
  - Número da nota, ordem da fatura, data de vencimento, marca, status
- Marque como "Emitida" quando necessário.
- Após todas as faturas de uma nota serem emitidas, a nota será arquivada automaticamente após 2 dias.

---

### 3. Filtrar faturas por período (US-13)

Facilita a localização de faturas por datas específicas.

**Como usar:**  
- Clique em **Filtrar** na tela principal.
- Selecione as datas de início e fim.
- Clique em **Aplicar Filtro** para ver as faturas do período.
- Clique em **Remover Filtro** para limpar a busca.

---

### 4. Filtrar faturas por marca (US-14, US-6)

Permite buscar faturas rapidamente por fornecedor.

**Como usar:**  
- Clique em **Filtrar** e selecione "Por Marca".
- Digite a marca e clique em **Pesquisar**.
- Veja as notas e faturas associadas à marca pesquisada.
- Se nenhuma for encontrada, uma mensagem será exibida.

---

### 5. Visualizar notas e faturas arquivadas (US-7)

Acesso a documentos já quitados (todas as faturas emitidas).

**Como usar:**  
- Acesse o menu de "Notas Arquivadas".
- Visualize a lista com:
  - Número da nota, quantidade de faturas, marca, data de arquivamento
- Utilize busca por número, marca ou data.

**Observação:**  
Apenas visualização, sem edição.  
Notas são arquivadas 2 dias após a última fatura ser emitida.

---

### 6. Receber alertas por e-mail de faturas a vencer (US-10, US-9)

O sistema envia e-mails automáticos para evitar atrasos em pagamentos.

**Como funciona:**  
- Alertas enviados para os e-mails cadastrados quando faltam até 3 dias para o vencimento da fatura.
- Os alertas são enviados diariamente até a fatura ser emitida.

**Como gerenciar e-mails:**  
- Acesse a tela de configuração de e-mails.
- Adicione ou remova endereços (sem duplicados ou e-mails inválidos).
- Todos os e-mails cadastrados recebem as notificações automaticamente.

---

## Checklist Rápido

- [ ] Acesso ao sistema realizado
- [ ] E-mails de alerta cadastrados corretamente
- [ ] Primeiras notas e faturas registradas
- [ ] Filtros de busca testados
- [ ] Recebimento de alertas conferido

---

## Melhorias Futuras

- Exportação de relatórios em PDF/Excel
- Histórico de alterações em cada nota
- Perfis de usuário e permissões diferenciadas
- Integração com sistemas contábeis externos
- Novos filtros e notificações visuais internas

---

## Envio de Feedback

Sugestões, dúvidas ou problemas podem ser enviados pela área de Issues do repositório ou para o e-mail:  
[suporte@guardoupagou.com](mailto:suporte@guardoupagou.com)

---

## Histórico de Versões

- **v1.0:** Funcionalidades principais implementadas (visualização, filtros, alertas)
- **v1.1:** Ajustes na tela de arquivadas e melhorias após feedback das sprints

---

*Este manual foi desenvolvido de forma incremental, evoluindo junto com o Guardou-Pagou e sempre orientado pelas práticas do Scrum e pelas necessidades dos usuários finais.*
