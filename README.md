# GuardouPagou

GuardouPagou é um sistema para registrar Notas Fiscais e controlar o pagamento de Faturas, com alertas automáticos por e-mail e filtros por período e por marca.

---

## 📚 Documentação

Para manter tudo organizado, a documentação está dividida em dois arquivos:

- 👉 **[Manual de Instalação](INSTALLATION_MANUAL.md)**
  Guia passo a passo para clonar o repositório, configurar o ambiente (Java, JavaFX, PostgreSQL, bibliotecas) e executar o projeto.


- 👉 **[Manual do Usuário](FEATURES_MANUAL.md)**
  Descrição das funcionalidades, fluxo de uso e telas do sistema.


- 👉 **[Guia de Contribuição](CONTRIBUTING.md)**
  Fluxo de trabalho com Git, convenções de branches e Pull Requests para colaboradores.

---

## 📂 Estrutura do Projeto

```
GuardouPagou/
libs/
├── java-database/
├── .../
│   └── java-email/
├── .../
│   └── javafx-sdk-21.0.7/
└── ...
resources/
├── css/
│   └── styles.css
├── fonts/
│   ├── Poppins-Bold.ttf
│   ├── Poppins-Medium.ttf
│   └── Poppins-Regular.ttf
├── icons/
│   ├── G-Clock(100x100px).png
│   ├── G-Clock_bg(100x100px).png
│   ├── G-Clock_home.png
│   ├── archive.png
│   ├── back.png
│   ├── calendar.png
│   ├── campaing.png
│   ├── cancel.png
│   ├── cancel_colored.png
│   ├── check.png
│   ├── check_colored.png
│   ├── clean.png
│   ├── edit.png
│   ├── filter_list.png
│   ├── filter_list_off.png
│   ├── list.png
│   ├── note-plus.png
│   ├── save.png
│   ├── user_add.png
│   └── user_remove.png
└── properties/
    ├── database.properties
    └── email.properties
src/
└── com/
    └── GuardouPagou/
        ├── controllers/
        │   ├── AlertaEmailController.java
        │   ├── ArquivadasController.java
        │   ├── EmailController.java
        │   ├── MainController.java
        │   ├── MarcaController.java
        │   ├── NotaFaturaController.java
        │   ├── NotaFiscalController.java
        │   └── NotaFiscalDetalhesController.java
        ├── dao/
        │   ├── AlertaEmailDAO.java
        │   ├── EmailDAO.java
        │   ├── FaturaDAO.java
        │   ├── MarcaDAO.java
        │   ├── NotaFiscalArquivadaDAO.java
        │   └── NotaFiscalDAO.java
        ├── models/
        │   ├── AlertaEmail.java
        │   ├── DatabaseConnection.java
        │   ├── Fatura.java
        │   ├── Main.java
        │   ├── Marca.java
        │   └── NotaFiscal.java
        ├── services/
        │   ├── AlertaService.java
        │   └── EmailSender.java
        └── views/
            ├── AlertaEmailView.java
            ├── ArquivadasView.java
            ├── EmailView.java
            ├── MainView.java
            ├── MarcaView.java
            ├── NotaFaturaView.java
            ├── NotaFiscalDetalhesView.java
            ├── NotaFiscalView.java
            └── ViewUtils.java
CONTRIBUTING.md
FEATURES_MANUAL.md
INSTALLATION_MANUAL.md
README.md
```
