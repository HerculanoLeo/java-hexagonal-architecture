package com.lodh8.starter;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ApplicationTest {

    ApplicationModules modules = ApplicationModules.of(Application.class);

    @Test
    void shouldBeCompliant() {
        modules.forEach(System.out::println);
        modules.verify();
    }

    @Test
    void writeDocumentationSnippets() {
        new Documenter(modules)
                .writeModuleCanvases()
                .writeModulesAsPlantUml()
                .writeDocumentation()
                .writeAggregatingDocument()
                .writeIndividualModulesAsPlantUml();
    }

}
