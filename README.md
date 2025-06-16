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
```

Em seguida, execute os comandos SQL abaixo para criar as tabelas:
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
    marca_id INTEGER, -- FK para marcas
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

UPDATE notas_fiscais n
SET marca_id = m.id
FROM marcas m
WHERE m.nome = n.marca;

```

-- Se migrando dados de um banco antigo:
```sql
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
```

---

## 💡 Observações

* Certifique-se de adicionar o `postgresql-42.7.5.jar` ao **classpath** do projeto no NetBeans ou no seu ambiente Java.
* Configure corretamente o **usuário, senha e URL** do banco de dados no seu código Java para a conexão funcionar.
* O arquivo `DataBaseConnection.java` está na raiz do projeto caso não tenha ou foi perdido.

---

## 📦 Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/Guardou-Pagou.git
cd Guardou-Pagou
```

