<#import "../fragments/components.ftlh" as components>

<#-- @ftlvariable name="titulo" type="java.lang.String" -->

<#macro main>
    <!DOCTYPE html>
    <html lang="pt-br">
    <@components.head title=titulo!"" />

    <body>
    <div class="wrapper">
        <!--[if mso]>
        <table align="center" border="0" cellspacing="0" cellpadding="0" width="600">
            <tr>
                <td align="center" valign="top" width="600">
        <![endif]-->
        <div class="container">
            <@components.header />

            <div class="content">
                <#nested>
            </div>

            <@components.footer />
        </div>
        <!--[if mso]>
        </td>
        </tr>
        </table>
        <![endif]-->
    </div>
    </body>
    </html>
</#macro>
