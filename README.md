# 🧾 Guardou-Pagou

Sistema de controle de faturas e notas fiscais com integração em Java, JavaFX e PostgreSQL.

---

## 🚀 Pré-requisitos

Antes de executar o projeto, instale os seguintes componentes:

- ☕ **Java JDK 21** e **JavaFX SDK 21.0.7**  
  Baixe via [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) ou [OpenJDK](https://jdk.java.net/21/)

- 🐘 **PostgreSQL 17.4-1**  
  [Download PostgreSQL](https://www.postgresql.org/download/)

- 🧬 **Driver JDBC**: `postgresql-42.7.5.jar`  
  Necessário para a conexão entre Java e o banco de dados PostgreSQL

- 🛠️ **Git** (para clonar o repositório)  
  [Download Git](https://git-scm.com/downloads)

- 🧩 **NetBeans 25** (opcional, mas recomendado)  
  [Download NetBeans](https://netbeans.apache.org/download/index.html)

---

## 🗄️ Banco de Dados

Crie um banco de dados com o nome:

```sql
CREATE DATABASE guardoupagou;
````

Em seguida, execute os comandos SQL abaixo para criar as tabelas:

```sql
CREATE TABLE marcas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(256),
    cor VARCHAR(7) NOT NULL
);

CREATE TABLE notas_fiscais (
    id SERIAL PRIMARY KEY,
    numero_nota VARCHAR(50) NOT NULL UNIQUE,
    data_emissao DATE NOT NULL,
    marca VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'Ativa',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE faturas (
    id SERIAL PRIMARY KEY,
    nota_fiscal_id INTEGER REFERENCES notas_fiscais(id) ON DELETE CASCADE,
    vencimento DATE NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Não Emitida',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 📂 Estrutura do Projeto

```
Guardou-Pagou/
├── src/
│   ├── main/
│   │   └── java/
│   └── resources/
├── lib/
│   └── postgresql-42.7.5.jar
├── README.md
└── ...
```

---

## 💡 Observações

* Certifique-se de adicionar o `postgresql-42.7.5.jar` ao **classpath** do projeto no NetBeans ou no seu ambiente Java.
* Configure corretamente o **usuário, senha e URL** do banco de dados no seu código Java para a conexão funcionar.

---

## 📦 Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/Guardou-Pagou.git
cd Guardou-Pagou
```

