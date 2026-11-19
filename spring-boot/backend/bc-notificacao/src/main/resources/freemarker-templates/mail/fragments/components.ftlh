<#import "../../resources/styles.ftlh" as styles>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nome" type="java.lang.String" -->
<#-- @ftlvariable name="url" type="java.lang.String" -->
<#-- @ftlvariable name="senha" type="java.lang.String" -->

<#macro head title>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <#if title?has_content>
            <title>${title}</title>
        <#else>
            <title>Notificação</title>
        </#if>
        <@styles.styles />
    </head>
</#macro>

<#macro header>
    <div class="header">
        <h1>Portal</h1>
    </div>
</#macro>

<#macro footer>
    <div class="footer">
        <p>&copy; ${.now?string('yyyy')} Portal. Todos os direitos reservados.</p>
    </div>
</#macro>

<#macro contatos>
    <div class="contact-box">
        <p>
            Se você tiver alguma dúvida sobre sua conta, entre em contato com o administrador do sistema.
        </p>
        <p>Obrigado,</p>
        <p><strong>Equipe Portal</strong></p>
    </div>
</#macro>

<#macro saudacao nome>
    <h2>Olá, ${nome}!</h2>
</#macro>

<#macro acessePortal url label="Acessar o sistema">
    <#if url?has_content>
        <div style="text-align: center; margin: 24px 0;">
            <a href="${url}" class="btn" rel="noopener noreferrer" target="_blank">${label}</a>
        </div>
    </#if>
</#macro>

<#macro senhaTemporaria senha>
    <#if senha?has_content>
        <p>Use a senha temporária abaixo no primeiro acesso:</p>
        <div class="credential-box">
            <p class="label">Senha temporária</p>
            <p class="value">${senha}</p>
        </div>
    </#if>
</#macro>
