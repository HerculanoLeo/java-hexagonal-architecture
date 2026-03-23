# Java Hexagonal Architecture - Modular Monolith

## 🎯 Objetivo do Projeto
Este projeto tem como objetivo demonstrar a implementação de uma arquitetura base robusta e escalável baseada no padrão **Monolito Modular**, empregando fortemente princípios de **Domain-Driven Design (DDD)** e **Arquitetura Hexagonal (Ports and Adapters)**.

A principal meta é manter as regras de negócio de domínio e as lógicas de aplicação puras e isoladas de frameworks de infraestrutura, bancos de dados e afins. Isso facilita a evolução do software, testes de unidade e uma eventual migração para microsserviços, caso necessário.

## 🏗️ Estrutura do Projeto
O repositório está organizado para suportar múltiplas abordagens de tecnologia, contendo pastas raiz para diferentes frameworks (`spring-boot/` e `quarkus/`).

Atualmente, o projeto está delineado em múltiplos módulos Maven no backend:

* **`shared-kernel`**: Código comum, interfaces e utilitários que são compartilhados e utilizados através de múltiplos domínios.
* **`domain-identity`**: Domínio responsável pelo gerenciamento de identidade e usuários.
* **`domain-authorize`**: Domínio focado em controle de acessos, papéis e permissões de usuários.
* **`domain-notification`**: Domínio para o gerenciamento de notificações e envios de comunicação.
* **`domain-location`**: Domínio que lida com dados de geolocalização e endereços.
* **`domain-plataformadmin`**: Domínio voltado para a administração geral da plataforma.
* **`api-cadastros`**: Módulo que serve de porta de entrada (Entrypoint/Adapter Driver) que agrupa as rotas e casos de uso de cadastros da aplicação.

## ✨ Funcionalidades Implementadas
O sistema aborda os seguintes escopos de negócio (contextos delimitados):
- **Gerenciamento de Identidade**: Autenticação, registro de usuários.
- **Autorização e Segurança**: Controle de acesso por roles (RBAC).
- **Notificações**: Estrutura base para disparo de notificações do sistema.
- **Localização**: Componentes de domínio de endereços e regiões.
- **Backoffice/Administração Plataforma**: Recursos para administradores globais gerenciarem operações sistêmicas.
- **Cadastros Gerais**: API agregadora contemplando os fluxos de cadastros da aplicação (ex: usuários, perfis, etc).

## 🛠️ Tecnologias e Bibliotecas Usadas
A implementação principal foi construída no ecosistema Java/Spring usando as seguintes tecnologias:

- **Java 25**
- **Spring Boot (3.5.x)**
- **Spring Modulith**: Garantia estrutural das fronteiras do monolito modular, verificações de dependência e arquitetura de eventos.
- **Spring Cloud**: Resiliência e integrações nativas de nuvem.
- **Keycloak (Admin Client)**: Gerenciamento unificado de identidade e acesso (IAM).
- **MapStruct e Lombok**: Geração de código limpo, mapeamento eficiente entre Entity e DTO.
- **Hibernate (via Hypersistence Utils)**: Tratamentos avançados de tipos em banco de dados relacional (JSON mappings, etc).
- **Caelum Stella Core**: Validações de domínio específicas do cenário brasileiro (CPF, CNPJ, etc).
- **Passay**: Imposição de políticas e verificação de forças de encriptação/geração de senhas.
- **SpringDoc OpenAPI**: Documentação automatizada via Swagger/OpenAPI 3.
- **Jacoco**: Relatório automatizado e métricas de cobertura de código.

## 🚧 Status de Implementação
* **Spring Boot**: Implementação primária (Avançada). Contém toda a estrutura base e a divisão de módulos (Monolito Modular e DDD).
* **Quarkus**: Implementação em progresso. O objetivo é espelhar a arquitetura proposta para avaliar os ganhos de performance e consumo de recursos proporcionados pelo Quarkus. Em breve a pasta `quarkus/backend` contará com o framework instanciado.
