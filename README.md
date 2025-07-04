

# 📘 Documentação: Como Rodar o Projeto Java `GuardouPagou`

## 📅 Clonando o Repositório

Você pode clonar o projeto diretamente do GitHub:

🔗 Repositório: [https://github.com/GBIFRS/GuardouPagou.git](https://github.com/GBIFRS/GuardouPagou.git)

### 📌 Comando:

```bash
git clone https://github.com/GBIFRS/GuardouPagou.git

```

> Certifique-se de ter o **Git** instalado na máquina antes de executar esse comando.

----------

## 📦 Pré-requisitos

Antes de executar o projeto, instale os seguintes componentes:

### ☕ Java

-   **Java JDK 21**

-   **JavaFX SDK 21.0.7**


📅 Baixe via Oracle JDK ou OpenJDK.

### 🐘 Banco de Dados

-   **PostgreSQL 17.4-1**


### 🔗 Bibliotecas Necessárias

-   `postgresql-42.7.5.jar` → Driver JDBC

-   `jakarta.mail-2.1.2.jar` → Para envio de e-mails

-   `jakarta.activation-2.1.2.jar` → Para anexos e conteúdos em e-mail


### 💻 IDE

-   **NetBeans 25** (opcional, mas recomendado) ou **IntelliJ IDEA**


----------

## 🛠️ Passo a Passo: NetBeans

### 1. Criar Projeto

-   Crie um novo projeto:  
    `Java with Ant -> Java Application`  
    Nome: `GuardouPagou`


### 2. Importar os Arquivos

-   Copie **todos os arquivos clonados** do repositório para o novo projeto.

-   No menu **Files**, arraste `DataBaseConnection.java` da raiz do projeto para o caminho:

    ```
    src/com/GuardouPagou/models/
    
    ```

    > Remova a extensão `.txt` do final do arquivo.


### 3. Configurar Pastas e Bibliotecas

-   Clique com botão direito no projeto > **Properties**:

  -   Em **Sources**:

    -   `Add Folder` > adicione as pastas `libs` e `resources`

  -   Em **Libraries > Modulepath**:

    -   Adicione todos os `.jar` nas seguintes pastas:

      -   `libs/java-database/`

      -   `libs/java-email/`

      -   `libs/javafx-sdk-21.0.7/lib/`


### 4. Configurar Compilação

-   Vá em **Compiling > Additional Compiler Options** e adicione:


```bash
--module-path "CaminhoCompleto\NetBeansProjects\GuardouPagou\libs\javafx-sdk-21.0.7\lib;CaminhoCompleto\NetBeansProjects\GuardouPagou\libs\jakarta.mail-2.0.1.jar" --add-modules javafx.controls,javafx.fxml,jakarta.mail

```

### 5. Configurar Execução

-   Em **Run > VM Options**, adicione o mesmo comando acima.

-   Defina a **Main Class** como:


```text
com.GuardouPagou.models.Main

```

----------

## 🗃️ Configurar o Banco de Dados (PostgreSQL)

1.  Acesse o **pgAdmin**

2.  Crie uma nova database chamada:


```text
guardoupagou

```

3.  Execute os comandos SQL abaixo:


```sql
CREATE TABLE marcas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(500),
    cor VARCHAR(7) NOT NULL
);

CREATE TABLE notas_fiscais (
    id SERIAL PRIMARY KEY,
    numero_nota VARCHAR(50) NOT NULL UNIQUE,
    data_emissao DATE NOT NULL,
    marca_id INTEGER,
    status VARCHAR(20) DEFAULT 'Ativa',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    arquivada BOOLEAN DEFAULT FALSE,
    data_arquivamento DATE,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_marca FOREIGN KEY (marca_id) REFERENCES marcas(id)
);

CREATE TABLE faturas (
    id SERIAL PRIMARY KEY,
    nota_fiscal_id INTEGER REFERENCES notas_fiscais(id) ON DELETE CASCADE,
    vencimento DATE NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    numero_fatura VARCHAR(50),
    status VARCHAR(20) DEFAULT 'Não Emitida',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE alerta_emails (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
);

```

----------

## 🔐 Configuração de E-mail no `project.properties`

1.  Abra o arquivo:


```
nbproject/project.properties

```

2.  Adicione ao final do arquivo:


```properties
run.jvmargs=-DMAIL_USER=seu_email@gmail.com -DMAIL_PASS="sua_senha_de_app" -DMAIL_HOST=smtp.gmail.com --module-path "CaminhoCompleto\GuardouPagou\libs\javafx-sdk-21.0.7\lib;CaminhoCompleto\GuardouPagou\libs\jakarta.mail-2.0.1.jar" --add-modules javafx.controls,javafx.fxml,jakarta.mail

```

----------

## ✉️ Configurar `EmailSender.java`

1.  Abra o arquivo `EmailSender.java`

2.  Descomente a parte **"Usando NetBeans"** dentro da classe `public class EmailSender`


----------

## ▶️ Executar o Projeto no NetBeans

Após concluir todos os passos, clique em **Run Project (F6)** para iniciar o GuardouPagou.

----------

## 💻 Passo a Passo: IntelliJ IDEA

### 1. Abrir o Projeto

-   Após clonar o repositório, abra a pasta pela IDE.


### 2. Configurar Estrutura do Projeto

-   Pressione: `Ctrl + Shift + Alt + S` → **Project Structure**

-   Vá em **Libraries**:

  -   Clique em ➕ e selecione os arquivos `.jar` nas pastas:

    -   `libs/java-database/`

    -   `libs/java-email/`

    -   `libs/javafx-sdk-21.0.7/lib/`

-   Vá em **Modules > Sources**:

  -   Marque:

    -   `src` como **Sources**

    -   `resources` como **Resources**

-   Vá em **Dependencies**:

  -   Verifique se:

    -   O **SDK é Java 21**

    -   As bibliotecas estão com escopo **Compile**


### 3. Configurar Execução

-   Vá em **Run > Edit Configurations**:

  -   Clique em ➕ → **Application**

    -   **Name**: `Main`

    -   **Main Class**: `com.GuardouPagou.models.Main`

    -   **VM Options**:


```bash
--module-path "CaminhoCompleto\GuardouPagou\libs\javafx-sdk-21.0.7\lib;CaminhoCompleto\GuardouPagou\libs\jakarta.mail-2.0.1.jar" --add-modules javafx.controls,javafx.fxml,jakarta.mail

```

-   **Environment Variables**:


```env
MAIL_HOST=smtp.gmail.com;MAIL_PASS=sua_senha_de_app;MAIL_USER=seu_email@gmail.com

```

----------

## 🗃️ Repetir Configuração do Banco de Dados (PostgreSQL)

Execute os **mesmos comandos SQL** da seção anterior para criar as tabelas no banco `guardoupagou`.

----------

## ▶️ Executar o Projeto no IntelliJ

Após tudo configurado, clique em **Run (Shift + F10)** para iniciar o projeto.


-- Se migrando dados de um banco antigo:
```sql
UPDATE notas_fiscais n
SET marca_id = m.id
FROM marcas m
WHERE m.nome = n.marca;

ALTER TABLE notas_fiscais ADD COLUMN marca_id INTEGER;

UPDATE notas_fiscais n
SET marca_id = m.id
FROM marcas m
WHERE m.nome = n.marca;

ALTER TABLE notas_fiscais
ADD CONSTRAINT fk_marca
FOREIGN KEY (marca_id)
REFERENCES marcas(id);

ALTER TABLE marcas
ALTER COLUMN descricao TYPE VARCHAR(500);
```
---


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

## Histórico de Versões

- **v1.0:** Funcionalidades principais implementadas (visualização, filtros, alertas)
- **v1.1:** Ajustes na tela de arquivadas e melhorias após feedback das sprints

---

# ℹ️ Outras Informações

## 📂 Estrutura do Projeto

```
GuardouPagou/
resources/
├── css/
│   └── styles.css
├── fonts/
│   ├── Poppins-Bold.ttf
│   ├── Poppins-Medium.ttf
│   └── Poppins-Regular.ttf
└── icons/
    ├── G-Clock(100x100px).png
    ├── G-Clock_bg(100x100px).png
    ├── G-Clock_home.png
    ├── archive.png
    ├── campaing.png
    ├── cancel.png
    ├── clean.png
    ├── list.png
    ├── plus.png
    └── save.png
src/
└── com/
    └── GuardouPagou/
        ├── controllers/
        │   ├── ArquivadasController.java
        │   ├── MainController.java
        │   ├── MarcaController.java
        │   ├── NotaFaturaController.java
        │   └── NotaFiscalController.java
        ├── dao/
        │   ├── FaturaDAO.java
        │   ├── MarcaDAO.java
        │   ├── NotaFiscalArquivadaDAO.java
        │   └── NotaFiscalDAO.java
        ├── models/
        │   ├── DatabaseConnection.java
        │   ├── Fatura.java
        │   ├── Main.java
        │   ├── Marca.java
        │   └── NotaFiscal.java
        └── views/
            ├── ArquivadasView.java
            ├── MainView.java
            ├── MarcaView.java
            ├── NotaFaturaView.java
            └── NotaFiscalView.java
CONTRIBUTING.md
README.md
build.xml
manifest.mf
