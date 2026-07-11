@ApplicationModule(
        id = "location",
        displayName = "Location",
        allowedDependencies = {
                "shared::events",
                "shared::exceptions",
                "shared::annotations",
                "shared::enums",
                "shared::utils",
        }
)
package com.lodh8.starter.location;

import org.springframework.modulith.ApplicationModule;