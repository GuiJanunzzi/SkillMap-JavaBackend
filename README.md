# 🚀 SkillMap - API de Gestão de Carreira & IA

O **SkillMap** é uma plataforma desenvolvida para enfrentar o desafio do **"Futuro do Trabalho"**. A solução empodera profissionais a gerenciarem sua jornada de requalificação (**Upskilling** e **Reskilling**) através de um mapeamento inteligente de competências.

O grande diferencial é o **Mentor de Carreira IA**, um assistente virtual integrado que utiliza **Inteligência Artificial Generativa** para analisar o perfil do usuário e sugerir planos de ação estratégicos e personalizados.

---

## 📦 Módulo Java (Backend)

Este módulo é o núcleo da aplicação, responsável por toda a regra de negócios, segurança, persistência de dados e integração com a IA.

### Principais Funcionalidades

* **API RESTful:** Fornecimento de dados e serviços para o aplicativo Mobile.
* **Spring AI:** Integração com LLMs (**Google Gemini**) para geração de conselhos de carreira personalizados.
* **Gestão de Entidades:** Gerenciamento de **Usuários**, **Habilidades**, **Metas** e **Categorias**.
* **Segurança:** Implementação de Autenticação Stateless via **JWT** (JSON Web Token).

---

## 👨‍💻 Equipe

| Nome | RM | Função |
| :--- | :--- | :--- |
| Caike Dametto | 558614 | Mobile & Frontend |
| Guilherme Janunzzi | 558461 | Backend & DevOps |

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos

Para rodar este projeto localmente, você precisará ter instalado:

* **Java 17** (JDK)
* **Maven 3.8+**
* **Conta no Google AI Studio** (necessário para a chave da API do Gemini)

### 🔧 Passo a Passo

#### 1. Clone o repositório

```bash
git clone https://github.com/GuiJanunzzi/SkillMap-JavaBackend.git

cd SkillMap-Java
```

### 2. Configure as Variáveis de Ambiente

O projeto utiliza variáveis de ambiente para dados sensíveis. Configure-as no seu Sistema Operacional ou na IDE:

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `GEMINI_KEY` | Sua chave de API do Google Gemini. **(Obrigatório)** | `AIzaSy...` |
| `DB_URL` | URL de conexão JDBC. | `jdbc:postgresql://host:port/dbname` |
| `DB_USER` | Usuário do Banco de Dados. | `postgres` |
| `DB_PSSWD` | Senha do Banco de Dados. | `123456` |

#### 3. Execute o projeto

Utilize o Maven Wrapper para iniciar a aplicação Spring Boot:

```bash
./mvnw spring-boot:run
```

#### 4. Acesse a API

O acesso pode ser feito localmente ou através do deploy na nuvem:

* **Local Base URL:** `http://localhost:8080`

**Deploy na Nuvem (Render)**

A aplicação está em deploy e pode ser acessada pelo link abaixo:

**Render Deploy URL:** [https://skillmap-javabackend.onrender.com](https://skillmap-javabackend.onrender.com)

> ⚠️ **Nota sobre o Deploy:** A API está hospedada no plano gratuito do Render. Se o servidor ficar inativo por um período, ele pode "dormir" (sleep) para economizar recursos. A primeira requisição feita pelo app (como o login) pode demorar **50 segundos ou mais** para "acordar" o servidor. Após a primeira conexão, a aplicação funcionará em velocidade normal.

---

## 🔗 Endpoints Principais

### 🔐 Autenticação (`/auth`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/auth/login` | Autentica o usuário e retorna o **Token JWT** |
| `POST` | `/auth/register` | Cadastra um novo usuário no sistema |

### 👤 Usuários & Perfil (`/usuarios`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/usuarios/{id}` | Retorna o perfil completo do usuário |
| `PUT` | `/usuarios/{id}` | Atualiza dados cadastrais (Nome/Email) |
| `POST` | `/usuarios/{id}/habilidades` | Adiciona uma habilidade ao perfil (Upskilling) |
| `POST` | `/usuarios/{id}/metas` | Adiciona uma meta de aprendizado (Reskilling) |
| `DELETE` | `/usuarios/{id}/habilidades/{habilidadeId}` | Remove uma habilidade do perfil |
| `DELETE` | `/usuarios/{id}/metas/{metaId}` | Remove uma meta de um usuario |

### 🤖 Inteligência Artificial (`/usuarios`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/usuarios/{id}/conselho-carreira` | Gera um plano de carreira personalizado via **IA** |

### 📚 Habilidades

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/habilidades` | Lista todas as habilidades disponíveis |
| `GET` | `/habilidades/{id}` | Busca uma habilidade pelo seu ID |
| `POST` | `/habilidades` | Cadastra nova habilidade |
| `PUT` | `/habilidades/{id}` | Atualiza uma habilidade existente |
| `DELETE` | `/habilidades/{id}` | Deleta uma habilidade (se não estiver em uso) |

### 📚 Categorias

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/categorias` | Lista categorias (ex: Tech, Soft Skill) |
| `GET` | `/categorias/{id}` | Busca uma categoria pelo seu ID |
| `POST` | `/categorias` | Adiciona uma nova categoria |
| `PUT` | `/categorias/{id}` | Atualiza uma categoria existente |
| `DELETE` | `/categorias/{id}` | Deleta uma categoria |

---

## 🧪 Testes e Qualidade

O projeto conta com cobertura de testes unitários utilizando **JUnit 5** e **Mockito**, focando nas regras de negócio da camada de Serviço (`Service`).

Para rodar os testes:

```bash
./mvnw test
```

## 📅 Licença

SkillMap © 2025 - FIAP Global Solution.
Todos os direitos reservados.