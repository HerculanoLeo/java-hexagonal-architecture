@ApplicationModule(
        id = "identity",
        displayName = "Identity",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::annotations",
                "shared::enums",
                "shared::utils",
        }
)
package com.herculanoleo.starter.identity;

import org.springframework.modulith.ApplicationModule;