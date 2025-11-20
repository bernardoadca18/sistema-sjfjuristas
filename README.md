# Sistema de Gestão de Empréstimos - SJF Juristas

![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Next JS](https://img.shields.io/badge/Next-black?style=for-the-badge&logo=next.js&logoColor=white)
![React Native](https://img.shields.io/badge/react_native-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Sobre o Projeto

O **Sistema SJF Juristas** é uma solução completa e robusta desenvolvida para a gestão, controle e operacionalização de empréstimos financeiros. A plataforma visa automatizar o fluxo de propostas, aprovações, desembolsos e acompanhamento de pagamentos, oferecendo interfaces específicas para administradores e clientes finais.

O ecossistema é composto por quatro pilares principais:
1.  **Backend API (Spring Boot):** O núcleo do sistema, responsável pela lógica de negócios, segurança e persistência de dados.
2.  **Admin Dashboard (Next.js):** Interface web para gestão administrativa (aprovação de propostas, relatórios, gestão de usuários).
3.  **Landing Page / Web (Next.js):** Portal público institucional.
4.  **Mobile App (React Native/Expo):** Aplicativo para clientes acompanharem seus empréstimos, visualizarem parcelas e realizarem pagamentos.

---

## Tecnologias Utilizadas

### Backend (`/backend`)
* **Linguagem:** Java 17+
* **Framework:** Spring Boot 3
* **Segurança:** Spring Security com JWT (JSON Web Token)
* **Banco de Dados:** PostgreSQL
* **Storage:** AWS S3 / MinIO (para armazenamento de documentos e comprovantes)
* **Documentação API:** Swagger / OpenAPI
* **Build Tool:** Maven

### Frontend Admin & Web (`/frontend`)
* **Framework:** Next.js (React)
* **Linguagem:** TypeScript
* **Estilização:** Tailwind CSS
* **Componentes:** Shadcn/ui (no dashboard administrativo)
* **Gerenciamento de Estado:** Context API / Hooks

### Mobile (`/frontend/sjf_juristas_emprestimos_app`)
* **Framework:** React Native com Expo
* **Linguagem:** TypeScript
* **Navegação:** Expo Router

### DevOps & Infraestrutura
* **Containerização:** Docker e Docker Compose
* **Proxy Reverso:** Nginx
* **Banco de Dados:** PostgreSQL (com scripts de seed e init)

---

## Funcionalidades Principais

* **Gestão de Usuários:**
    * Cadastro de clientes e administradores.
    * Autenticação segura e redefinição de senha via e-mail.
    * Perfis de acesso diferenciados (RBAC).
* **Fluxo de Propostas:**
    * Solicitação de empréstimo via App ou Web.
    * Upload de documentos comprobatórios.
    * Análise, aprovação ou recusa por administradores.
    * Contrapropostas e ajustes de condições.
* **Gestão Financeira:**
    * Cálculo de parcelas e juros.
    * Acompanhamento de status de pagamento (Pendente, Pago, Atrasado).
    * Integração com chaves PIX para pagamentos.
    * Upload e validação de comprovantes de pagamento.
* **Auditoria e Notificações:**
    * Logs de auditoria para ações sensíveis.
    * Sistema de notificações para o usuário.

---

## Pré-requisitos

Para executar o projeto localmente, você precisará ter instalado:

* [Docker](https://www.docker.com/) e Docker Compose (Recomendado para rodar todo o ambiente).
* [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/) (Para execução manual do backend).
* [Node.js LTS](https://nodejs.org/) (Para os frontends e mobile).
* [Maven](https://maven.apache.org/) (Opcional, pois o projeto possui `mvnw`).

---

## Como Rodar o Projeto

### Opção 1: Via Docker Compose (Recomendado)

O projeto possui um arquivo `docker-compose.yaml` na raiz que orquestra o banco de dados e os serviços.

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/sistema-sjfjuristas.git](https://github.com/seu-usuario/sistema-sjfjuristas.git)
    cd sistema-sjfjuristas
    ```

2.  **Configuração de Variáveis de Ambiente:**
    Verifique o arquivo `.env.example` na raiz e crie um arquivo `.env` com as configurações necessárias (credenciais de banco, chaves secretas, etc.).
    ```bash
    cp .env.example .env
    ```

3.  **Subir os containers:**
    ```bash
    docker-compose up --build -d
    ```
    *Isso iniciará o PostgreSQL, o Backend e possivelmente os Frontends dependendo da configuração do compose.*

### Opção 2: Execução Manual

#### 1. Banco de Dados
Suba uma instância do PostgreSQL e crie um banco de dados (ex: `sjf_juristas`). Certifique-se de executar os scripts da pasta `db/` para criar as tabelas e popular os dados iniciais.

#### 2. Backend
```bash
cd backend
# Configure o arquivo application.properties ou application-dev.properties com seu banco de dados
./mvnw spring-boot:run
```
A API estará disponível em: http://localhost:8080

#### 3. Frontend Admin Dashboard
```bash
cd frontend/sjf-juristas-admin_dashboard
npm install
npm run dev
```
Acesse em: http://localhost:3000

#### 4. Frontend Landing Page
```bash
cd frontend/sjf-juristas-next
npm install
npm run dev
```
Acesse em: http://localhost:(porta definida)

#### 5. Mobile App
```bash
cd frontend/sjf_juristas_emprestimos_app
npm install
npx expo start
```


#### Estrutura de Diretórios
```bash
sistema-sjfjuristas/
├── backend/                  # Código fonte da API Spring Boot
├── db/                       # Scripts SQL de migração e seed
├── frontend/
│   ├── sjf-juristas-admin_dashboard/ # Painel Administrativo (Next.js)
│   ├── sjf-juristas-next/            # Site Institucional (Next.js)
│   └── sjf_juristas_emprestimos_app/ # App Mobile (React Native/Expo)
├── nginx/                    # Configurações do Proxy Reverso
├── docker-compose.yaml       # Orquestração de containers
└── .env.example              # Modelo de variáveis de ambiente
```


#### Documentação da API
Com o backend rodando, você pode acessar a documentação interativa do Swagger/OpenAPI através do endereço:
```bash
http://localhost:8080/swagger-ui.html
```
Ou a especificação JSON em:
```bash
http://localhost:8080/v3/api-docs
```
