<#import "../../layout/main-layout.ftlh" as layout>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nomeUsuario" type="java.lang.String" -->

<@layout.main>
    <h2>Olá, ${nomeUsuario}!</h2>
    <p>Este é um e-mail de teste da nossa plataforma.</p>
    <p>Se você está lendo este e-mail, o envio foi um sucesso.</p>
</@layout.main>
