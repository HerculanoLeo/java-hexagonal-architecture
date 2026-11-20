<#import "../fragments/components.ftlh" as components>
<#import "../layout/main-layout.ftlh" as layout>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nome" type="java.lang.String" -->

<@layout.main>
    <@components.saudacao nome=nome />

    <p>Seu acesso ao portal foi inativado.</p>

    <div class="info-box">
        <p>Caso acredite que isso foi um engano, entre em contato com o administrador do sistema.</p>
    </div>

    <@components.contatos />
</@layout.main>
