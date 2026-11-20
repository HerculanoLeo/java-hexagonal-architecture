@ApplicationModule(
        id = "notificacao",
        displayName = "Notification",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::enums",
                "shared::annotations",
                "shared::utils",
        }
)
package com.lodh8.starter.notificacao;

import org.springframework.modulith.ApplicationModule;