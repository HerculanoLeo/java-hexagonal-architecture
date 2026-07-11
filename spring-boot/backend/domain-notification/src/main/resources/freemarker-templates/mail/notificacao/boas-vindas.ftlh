<#import "../fragments/components.ftlh" as components>
<#import "../layout/main-layout.ftlh" as layout>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nome" type="java.lang.String" -->
<#-- @ftlvariable name="senha" type="java.lang.String" -->
<#-- @ftlvariable name="url" type="java.lang.String" -->

<@layout.main>
    <@components.saudacao nome=nome />

    <p>Seja bem-vindo(a) ao nosso portal. Estamos felizes em ter você conosco.</p>

    <@components.senhaTemporaria senha=senha!"" />

    <@components.acessePortal url=url />

    <@components.contatos />
</@layout.main>
