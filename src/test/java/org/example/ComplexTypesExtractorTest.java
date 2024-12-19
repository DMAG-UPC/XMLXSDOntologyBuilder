package org.example;

import org.example.ontology.ObjectFactory;
import org.example.ontology.RDFType;
import org.junit.Assert;
import org.junit.Test;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdSchema;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.example.ComplexTypesExtractor.getComplexTypes;
import static org.junit.Assert.*;

public class ComplexTypesExtractorTest {

    @Test
    public void testGetComplexTypes() {
        XsdParser parserInstance1 = new XsdParser("src/test/resources/TestComplexTypesExtractor.xsd");

        Map<String, XsdComplexType> complexTypes = getComplexTypes(
                parserInstance1.getResultXsdSchemas().toList().getFirst()
        );

        Assert.assertEquals(6, complexTypes.size());
    }
}