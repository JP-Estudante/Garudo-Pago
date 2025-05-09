# Guardou-Pagou
Instale os seguintes componentes:

Java JDK 21 e javafx-sdk-21.0.7: Download Oracle JDK ou OpenJDK.

PostgreSQL 17.4-1: Download PostgreSQL.

Git (para clonar o repositório): Download Git.

NetBeans 25 (opcional, se for usar a IDE): Download NetBeans.

JDBC Driver: postgresql-42.7.5.jar

Criar banco de dados guardoupagou.
Tabelas:

CREATE TABLE faturas (
    id SERIAL PRIMARY KEY,
    nota_fiscal_id INTEGER REFERENCES notas_fiscais(id) ON DELETE CASCADE,
    vencimento DATE NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Não Emitida',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
CREATE TABLE marcas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(256),
    cor VARCHAR(7) NOT NULL
);

