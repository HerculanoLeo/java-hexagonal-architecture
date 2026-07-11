@ApplicationModule(
        id = "authorize",
        displayName = "Authorize",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::annotations",
                "shared::enums",
                "shared::utils",
                "identity::usuarios",
                "identity::usuarios-domain",
                "identity::grupos",
                "identity::infra-attributes",
                "notification::events",
        }
)
package com.lodh8.starter.authorize;

import org.springframework.modulith.ApplicationModule;