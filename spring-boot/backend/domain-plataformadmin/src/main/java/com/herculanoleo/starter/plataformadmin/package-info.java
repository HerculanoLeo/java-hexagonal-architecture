@ApplicationModule(
        id = "plataformadmin",
        displayName = "Plataform Admin",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::enums",
                "shared::annotations",
                "shared::utils",
                "identity::infra-attributes",
                "identity::usuarios",
                "identity::usuarios-domain",
                "identity::grupos",
                "identity::grupos-domain",
                "identity::roles",
                "identity::roles-domain",
                "authorize::authenticated-user",
                "authorize::domain",
                "notification::events"
        }
)
package com.herculanoleo.starter.plataformadmin;

import org.springframework.modulith.ApplicationModule;