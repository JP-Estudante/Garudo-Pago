# 📘 Manual de instalação - GuardouPagou

## Clonando o Repositório

Você pode clonar o projeto diretamente do GitHub:

🔗 Repositório: [https://github.com/GBIFRS/GuardouPagou.git](https://github.com/GBIFRS/GuardouPagou.git)

### 📌 Comando:

```bash
git clone https://github.com/GBIFRS/GuardouPagou.git
```

> Certifique-se de ter o **Git** instalado na máquina antes de executar esse comando.

---

## 📦 Pré-requisitos

Antes de executar o projeto, instale os seguintes componentes:

### ☕ Java

* **Java JDK 21**

* **JavaFX SDK 21.0.7**

📅 Baixe via Oracle JDK ou OpenJDK.

### 🐘 Banco de Dados

* **PostgreSQL 17.4-1**

### 🔗 Bibliotecas Necessárias

* `postgresql-42.7.5.jar` → Driver JDBC

* `jakarta.mail-2.1.2.jar` → Para envio de e-mails

* `jakarta.activation-2.1.2.jar` → Para anexos e conteúdos em e-mail

### IDE

* **NetBeans 25** (opcional, mas recomendado) ou **IntelliJ IDEA Community Edition**

---

## Configurar o Banco de Dados (PostgreSQL)

1. Acesse o **pgAdmin**.
2. Crie uma nova database chamada: `guardoupagou`

3.  Execute os comandos SQL abaixo para criar as tabelas:

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

## Passo a Passo: NetBeans

### 1. Criar Projeto

-   Crie um novo projeto:  
    `Java with Ant -> Java Application`  
    **Nome:** `GuardouPagou`

### 2. Importar os Arquivos

-   Copie **todos os arquivos clonados** do repositório para o novo projeto.

### 3. Configurar Pastas e Bibliotecas

-   Clique com botão direito no projeto > **Properties**:
    -   Em **Sources**:
        -   `Add Folder` > adicione as pastas `libs` e `resources`.
    -   Em **Libraries > Modulepath**:
        -   Adicione todas os `.jar` nas pastas:
            -   `libs/java-database/`
            -   `libs/java-email/`
            -   `libs/javafx-sdk-21.0.7/lib/`

### 4. Criar Arquivos de Propriedades

1.  No tree do projeto, expanda a pasta `resources` e crie uma subpasta chamada `properties`.
2.  Dentro de `resources/properties/`, crie os arquivos:
    -   **database.properties**
    -   **email.properties**

<a name="snippet-database.properties"></a>
3.  Exemplo do conteúdo para **database.properties**:

    ```properties
    # Conexão com PostgreSQL
    db.url=jdbc:postgresql://localhost:5432/guardoupagou
    db.username=postgres
    db.password=sua_senha
    ```
    
<a name="snippet-email.properties"></a>
4.  Exemplo de conteúdo para **email.properties**:

    ```properties
    # Configuração de e-mail SMTP
    mail.host=smtp.gmail.com
    mail.port=587
    mail.user=seu_email@gmail.com
    mail.pass=sua_senha_de_app
    mail.auth=true
    mail.starttls.enable=true
    ```

> Esses arquivos serão carregados automaticamente pelo sistema, se o diretório `resources/` estiver marcado como Resource Folder.

### 5. Configurar Execução

-   Em **Run > VM Options**, substitua qualquer configuração anterior de e-mail por este comando (ajuste os paths conforme seu SO):

    ```bash
    --module-path "CaminhoCompleto/GuardouPagou/libs/javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml,jakarta.mail
    ```

### 6. Executar o Projeto

Após concluir todos os passos, clique em **Run Project (F6)** para iniciar o GuardouPagou.

----------

## Passo a Passo: IntelliJ IDEA

### 1. Abrir o Projeto

-   Após clonar o repositório, abra a pasta do projeto na IDE.

### 2. Configurar Estrutura do Projeto

1.  Pressione `Ctrl + Shift + Alt + S` → **Project Structure**.
2.  Em **Modules > Sources**:
    -   Marque `src` como **Sources**.
    -   Marque `resources` como **Resources**.
3.  Em **Libraries**:
    -   Clique em ➕ e selecione todos os `.jar` das pastas:
        -   `libs/java-database/`
        -   `libs/java-email/`
        -   `libs/javafx-sdk-21.0.7/lib/`
4.  Verifique em **Dependencies** que o **SDK é Java 21** e as bibliotecas estão com escopo **Compile**.

### 3. Criar Arquivos de Propriedades

-   Dentro de `src/main/resources/`, crie a pasta `properties` e adicione:
  -   **database.properties** (mesmo conteúdo do exemplo [acima](snippet-database.properties))
  -   **email.properties** (mesmo conteúdo do exemplo [acima](snippet-email.properties))

### 4. Configurar Execução

1.  Vá em **Run > Edit Configurations** → selecione a configuração `Main` (ou crie uma nova Application).
2.  Em **VM Options**, adicione:

    ```bash
    --module-path "CaminhoCompleto/GuardouPagou/libs/javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml,jakarta.mail
    ```

3.  **Environment Variables:** (não é necessário definir variáveis de e-mail)

> Como o diretório `resources/` está no classpath, os arquivos `.properties` serão carregados automaticamente em tempo de execução.

### 5. Executar o Projeto

Após tudo configurado, clique em **Run (Shift + F10)** para iniciar o GuardouPagou.