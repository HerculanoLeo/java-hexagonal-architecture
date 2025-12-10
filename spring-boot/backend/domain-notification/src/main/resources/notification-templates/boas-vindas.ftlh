<#-- @ftlvariable name="titulo" type="java.lang.String" -->
<#-- @ftlvariable name="nome" type="java.lang.String" -->
<#-- @ftlvariable name="senha" type="java.lang.String" -->
<#-- @ftlvariable name="url" type="java.lang.String" -->

<!DOCTYPE html>
<html lang="pt-Br">
<head>
    <title>
        ${titulo}
    </title>
</head>

<body>
<p>Seja Bem-vindo(a) ao nosso portal, ${nome}!!!</p>
<#if senha??>
    <p>Para seu primeiro acesso use a senha temporária abaixo:</p>
    <p>${senha}</p>
</#if>
<p>Acesse o sistema aqui: <a href="${url}" rel="noopener noreferrer" target="_blank">link</a></p>
</body>
</html>
