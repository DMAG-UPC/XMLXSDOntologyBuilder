package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;

import java.util.Map;

import static org.example.ComplexTypesExtractor.getComplexTypes;

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