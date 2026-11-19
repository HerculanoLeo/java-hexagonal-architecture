<#import "../fragments/components.ftlh" as components>
<#import "../layout/main-layout.ftlh" as layout>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nome" type="java.lang.String" -->
<#-- @ftlvariable name="senha" type="java.lang.String" -->
<#-- @ftlvariable name="url" type="java.lang.String" -->

<@layout.main>
    <@components.saudacao nome=nome />

    <p>Sua senha foi reiniciada com sucesso.</p>

    <@components.senhaTemporaria senha=senha!"" />

    <div class="info-box">
        <p>Por segurança, altere esta senha temporária assim que acessar o sistema.</p>
    </div>

    <@components.acessePortal url=url />

    <@components.contatos />
</@layout.main>
