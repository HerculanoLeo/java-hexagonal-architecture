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
<p>Senha reiniciada com sucesso!</p>
<p>Nome: ${nome}</p>
<p>Nova senha temporária: ${senha}</p>
<p>Acesse o sistema aqui: <a href="${url}" rel="noopener noreferrer" target="_blank">link</a></p>
</body>
</html>
