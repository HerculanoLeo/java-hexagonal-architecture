@ApplicationModule(
        id = "seguranca",
        displayName = "Security",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::annotations",
                "shared::enums",
                "shared::utils",
                "authorize::authenticated-user",
                "authorize::domain",
                "plataforma::usuarios",
                "plataforma::usuarios-domain",
                "identity::usuarios",
        }
)
package com.lodh8.starter.seguranca;

import org.springframework.modulith.ApplicationModule;
