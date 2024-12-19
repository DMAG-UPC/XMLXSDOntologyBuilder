package org.example;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XSDSchemaToOntologySchemaTest {

    @Test
    public void testOneComplex() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_OneComplexType.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_OneComplexType.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testTwoComplexes() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_TwoComplexTypes.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_TwoComplexTypes.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testThreeComplexes() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ThreeComplexTypes.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_ThreeComplexTypes.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testExtendedComplexes() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ExtendedType.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_ExtendedType.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testExtensionEmpty() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ExtensionEmpty.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_ExtensionEmpty.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testExtendedComplexWithChoice() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ExtendedTypeWithChoice.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_ExtendedTypeWithChoice.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testExtendedComplexOnlyChoice() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ExtendedTypeOnlyChoice.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_ExtendedTypeOnlyChoice.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testChoiceComplexType() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ChoiceComplexTypes.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_Choice.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testRestriction() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_SimpleTypeRestriction.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_SimpleTypeRestriction.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testMinimumRequirementsSchema() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_minimum_requirements.xsd";

        try (FileOutputStream fileOutputStream = new FileOutputStream("src/test/resources/generated_Testing_minimum_requirements.xml")) {
            XSDSchemaToOntologySchema.transform(filePath, fileOutputStream);
        }
    }


    @Test
    public void testExtensionAndRestriction() throws IOException, SAXException {
        String filePath = "src/test/resources/Testing_ExtensionWithSimpleTypeRestriction.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XSDSchemaToOntologySchema.transform(filePath, outputStream);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/expected_Testing_ExtensionWithSimpleTypeRestriction.xml")) {
            diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }

    @Test
    public void testEntireRequirementsSchema() throws IOException, SAXException {
        String filePath = "src/main/resources/input/RequirementsSchemas.xsd";

        try (FileOutputStream fileOutputStream = new FileOutputStream("src/test/resources/generated_Testing_EntireRequirements.xml")) {
            XSDSchemaToOntologySchema.transform(filePath, fileOutputStream);
        }

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (
                FileInputStream generatedFileInputStream = new FileInputStream("src/test/resources/generated_Testing_EntireRequirements.xml");
                FileInputStream expectedFileInputStream = new FileInputStream("src/test/resources/expected_Testing_EntireRequirements.xml")
        ) {
            diff = XMLUnit.compareXML(
                    new String(expectedFileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    new String(generatedFileInputStream.readAllBytes(), StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());
    }
}