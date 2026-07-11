@ApplicationModule(
        id = "plataformadmin",
        displayName = "Platform Admin",
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
package com.lodh8.starter.backoffice;

import org.springframework.modulith.ApplicationModule;