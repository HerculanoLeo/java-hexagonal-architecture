@ApplicationModule(
        id = "notification",
        displayName = "Notification",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::enums",
                "shared::annotations",
                "shared::utils",
        }
)
package com.lodh8.starter.notification;

import org.springframework.modulith.ApplicationModule;