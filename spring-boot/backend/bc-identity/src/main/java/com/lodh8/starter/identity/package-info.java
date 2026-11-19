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
package com.lodh8.starter.identity;

import org.springframework.modulith.ApplicationModule;