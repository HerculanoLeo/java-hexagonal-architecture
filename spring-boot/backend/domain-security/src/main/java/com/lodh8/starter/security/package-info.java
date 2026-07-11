@ApplicationModule(
        id = "security",
        displayName = "Security",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::annotations",
                "shared::enums",
                "shared::utils",
                "authorize::authorize-ports",
                "authorize::domain",
                "plataformadmin::usuarios",
                "plataformadmin::usuarios-domain",
                "identity::usuarios",
        }
)
package com.lodh8.starter.security;

import org.springframework.modulith.ApplicationModule;
