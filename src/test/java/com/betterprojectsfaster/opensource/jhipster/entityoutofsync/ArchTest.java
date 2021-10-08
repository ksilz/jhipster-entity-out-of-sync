package com.betterprojectsfaster.opensource.jhipster.entityoutofsync;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.betterprojectsfaster.opensource.jhipster.entityoutofsync");

        noClasses()
            .that()
            .resideInAnyPackage("com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service..")
            .or()
            .resideInAnyPackage("com.betterprojectsfaster.opensource.jhipster.entityoutofsync.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.betterprojectsfaster.opensource.jhipster.entityoutofsync.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
