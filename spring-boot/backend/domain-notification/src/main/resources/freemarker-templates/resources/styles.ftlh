<#macro styles>
    <style type="text/css">
        /* Reset e compatibilidade com clientes de e-mail */
        body, table, td, a {
            -webkit-text-size-adjust: 100%;
            -ms-text-size-adjust: 100%;
        }

        table, td {
            mso-table-lspace: 0pt;
            mso-table-rspace: 0pt;
        }

        img {
            -ms-interpolation-mode: bicubic;
            border: 0;
            height: auto;
            line-height: 100%;
            outline: none;
            text-decoration: none;
        }

        table {
            border-collapse: collapse !important;
        }

        body {
            height: 100% !important;
            margin: 0 !important;
            padding: 0 !important;
            width: 100% !important;
            font-family: Arial, Helvetica, sans-serif;
            background-color: #f4f4f5;
            color: #3f3f46;
        }

        /* Estrutura */
        .wrapper {
            width: 100%;
            background-color: #f4f4f5;
            padding: 24px 0;
        }

        .container {
            display: block;
            margin: 0 auto !important;
            max-width: 600px;
            background-color: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
        }

        .content {
            padding: 32px;
        }

        /* Tipografia */
        h1, h2, h3 {
            color: #27272a;
            margin-top: 0;
            font-weight: bold;
        }

        h1 {
            font-size: 24px;
            margin-bottom: 20px;
        }

        h2 {
            font-size: 20px;
            margin-bottom: 16px;
        }

        p {
            font-size: 16px;
            line-height: 1.55;
            margin-bottom: 14px;
            color: #52525b;
        }

        a {
            color: #3f3f46;
            text-decoration: underline;
        }

        /* Header / Footer */
        .header {
            background-color: #3f3f46;
            padding: 24px 20px;
            text-align: center;
            color: #ffffff;
        }

        .header h1 {
            color: #ffffff;
            margin: 0;
            font-size: 22px;
            font-weight: bold;
            letter-spacing: 0.02em;
        }

        .footer {
            background-color: #fafafa;
            padding: 20px;
            text-align: center;
            font-size: 12px;
            color: #a1a1aa;
            border-top: 1px solid #e4e4e7;
        }

        .footer p {
            margin: 0;
            font-size: 12px;
            color: #a1a1aa;
        }

        /* Componentes */
        .contact-box {
            background-color: #f4f4f5;
            padding: 20px;
            border-radius: 6px;
            text-align: left;
            margin-top: 28px;
        }

        .contact-box p {
            margin-bottom: 8px;
            font-size: 14px;
            color: #71717a;
        }

        .btn {
            display: inline-block;
            padding: 12px 28px;
            background-color: #3f3f46;
            color: #ffffff !important;
            border-radius: 6px;
            text-decoration: none !important;
            font-weight: bold;
            font-size: 15px;
            margin: 8px 0;
        }

        .credential-box {
            background-color: #fafafa;
            border: 1px solid #e4e4e7;
            border-radius: 6px;
            padding: 16px 20px;
            margin: 16px 0 24px;
            text-align: center;
        }

        .credential-box .label {
            font-size: 13px;
            color: #71717a;
            margin-bottom: 8px;
        }

        .credential-box .value {
            font-family: Consolas, Monaco, monospace;
            font-size: 18px;
            font-weight: bold;
            color: #27272a;
            letter-spacing: 0.04em;
            margin: 0;
        }

        .info-box {
            background-color: #fafafa;
            border-left: 3px solid #a1a1aa;
            padding: 14px 18px;
            margin: 20px 0;
        }

        .info-box p {
            margin-bottom: 0;
            font-size: 14px;
            color: #71717a;
        }

        /* Responsividade */
        @media only screen and (max-width: 620px) {
            .container {
                width: 100% !important;
                border-radius: 0;
            }

            .content {
                padding: 24px 20px;
            }
        }
    </style>
</#macro>
