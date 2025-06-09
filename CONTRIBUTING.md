# 🧑‍💻 Como trabalhar com o Projeto GuardouPagou

Este guia define o fluxo de trabalho padrão com Git para mantermos o código organizado, evitar conflitos e facilitar a colaboração entre os desenvolvedores.

## ✅ Regras Básicas

- **Nunca trabalhe diretamente na branch `main`**.
- **Cada tarefa deve ter sua própria branch**.
- Use nomes claros e padronizados para as branches:  
  - `feat/` para novas funcionalidades  
  - `fix/` para correções  
  - `refactor/` para melhorias internas  

## 🧭 Passo a Passo para Trabalhar com Git

### 1. Atualize seu repositório local

Sempre comece atualizando sua branch principal local:

```bash
git checkout main
git pull origin main
```

---

### 2. Crie uma nova branch para sua tarefa

```bash
git checkout -b feat/nome-da-funcionalidade
# ou
git checkout -b fix/nome-da-correcao
```

Exemplos:

```bash
git checkout -b feat/cadastro-marcas
git checkout -b fix/erro-visualizacao-fatura
```

---

### 3. Faça alterações e commits pequenos e claros

```bash
git add .
git commit -m "Adiciona tela de cadastro de marcas"
```

Faça commits frequentes com mensagens descritivas.

---

### 4. Mantenha sua branch atualizada com a `main`

Antes de dar `push` ou com frequência para evitar conflitos:

```bash
git checkout main
git pull origin main
git checkout sua-branch
git merge main
```

Resolva qualquer conflito, se necessário.

---

### 5. Suba sua branch para o repositório remoto

```bash
git push origin nome-da-sua-branch
```

---

### 6. Crie um Pull Request (PR)

- Vá até o GitHub e abra um **Pull Request para a `main`**.
- Alguém da equipe com o papel de QA irá revisar.
- Após aprovação, o merge será feito.

---

### 7. Após o Merge, atualize seu ambiente

```bash
git checkout main
git pull origin main
git branch -d sua-branch               # (Remove localmente)
git push origin --delete sua-branch   # (Remove remotamente, opcional)
```

## ❌ O que fazer se o PR (Pull Request) for reprovado

### Se o QA ou revisor identificar erros:

- O QA deve comentar diretamente no PR o que precisa ser ajustado.
- O desenvolvedor faz os ajustes na **mesma branch** e envia novos commits:

```bash
git add .
git commit -m "Corrige validação do formulário"
git push origin nome-da-sua-branch
```

O PR será atualizado automaticamente.

### Se a funcionalidade for cancelada:

- O PR deve ser **fechado sem fazer merge**.
- A branch pode ser removida:

```bash
git branch -d nome-da-sua-branch               # Remove localmente
git push origin --delete nome-da-sua-branch    # Remove remotamente
```

## 📌 Boas Práticas

- Sempre **puxe (`pull`) antes de começar a trabalhar**.
- Commit pequeno, claro e frequente é melhor que um gigante.
- Nome de branch deve refletir a tarefa.
