package org.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ComplexTypesExtractorTest.class,
        FilteringRequirements.class,
        NamedIndividualBuilderTest.class,
        AddModificationDatesTest.class,
        XSDSchemaToOntologySchemaTest.class,
        XSDSchemaAndXMLToOntologyTest.class,
        OntologiesRelationerTest.class
})
public class AllOntologyGenerationTests {

}

