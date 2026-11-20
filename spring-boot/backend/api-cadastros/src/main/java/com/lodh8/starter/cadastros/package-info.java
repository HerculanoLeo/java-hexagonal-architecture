@ApplicationModule(
        id = "cadastros",
        displayName = "Composition Root",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::enums",
                "shared::annotations",
                "shared::utils",
                "identity::usuarios",
                "identity::usuarios-domain",
                "identity::grupos",
                "identity::grupos-domain",
                "identity::roles",
                "identity::roles-domain",
                "identity::attributes",
                "authorize::authenticated-user",
                "authorize::domain",
                "plataforma::usuarios",
                "plataforma::usuarios-domain",
                "notificacao::app",
                "notificacao::domain",
                "notificacao::events",
        }
)
package com.lodh8.starter.cadastros;

import org.springframework.modulith.ApplicationModule;
