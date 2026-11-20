@ApplicationModule(
        id = "localizacao",
        displayName = "Location",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::annotations",
                "shared::enums",
                "shared::utils",
        }
)
package com.lodh8.starter.localizacao;

import org.springframework.modulith.ApplicationModule;