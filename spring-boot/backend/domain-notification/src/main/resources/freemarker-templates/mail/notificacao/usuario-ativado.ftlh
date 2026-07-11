<#import "../fragments/components.ftlh" as components>
<#import "../layout/main-layout.ftlh" as layout>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nome" type="java.lang.String" -->
<#-- @ftlvariable name="url" type="java.lang.String" -->

<@layout.main>
    <@components.saudacao nome=nome />

    <p>Seu acesso ao portal foi reativado.</p>
    <p>Você já pode entrar novamente no sistema com suas credenciais.</p>

    <@components.acessePortal url=url />

    <@components.contatos />
</@layout.main>
