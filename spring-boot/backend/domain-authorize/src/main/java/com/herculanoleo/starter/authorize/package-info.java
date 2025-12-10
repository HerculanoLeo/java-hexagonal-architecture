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
                "identity::grupos",
                "identity::infra-attributes",
        }
)
package com.herculanoleo.starter.authorize;

import org.springframework.modulith.ApplicationModule;