@ApplicationModule(
        id = "plataforma",
        displayName = "Platform Admin",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::enums",
                "shared::annotations",
                "shared::utils",
                "identity::attributes",
                "identity::usuarios",
                "identity::usuarios-domain",
                "identity::grupos",
                "identity::grupos-domain",
                "identity::roles",
                "identity::roles-domain",
                "authorize::authenticated-user",
                "authorize::domain",
                "notificacao::events"
        }
)
package com.lodh8.starter.plataforma;

import org.springframework.modulith.ApplicationModule;
