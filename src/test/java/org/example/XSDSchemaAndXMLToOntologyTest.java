package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.NamedIndividualBuilderTest.check_output;

public class XSDSchemaAndXMLToOntologyTest {
    @Test
    public void testValidateDOMFile() {
        File xmlFile = new File("src/test/resources/oneComplexType_example1.xml");
        File xsdFile = new File("src/test/resources/Testing_OneComplexType.xsd");
        try {
            Assert.assertTrue(XSDSchemaAndXMLToOntology.validateDom(xmlFile, xsdFile));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void transform_oneComplexType() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/oneComplexType_example2.xml");
        String xsdFilePath = "src/test/resources/Testing_OneComplexType.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_rdf_OneComplexTypeTwoEntries.xml");
    }

    @Test
    public void transform_twoComplexTypes() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/twoComplexTypes_example1.xml");
        String xsdFilePath = "src/test/resources/Testing_TwoComplexTypes.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_rdf_TwoComplexTypes.xml");
    }

    @Test
    public void transform_choiceComplexType() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/choiceComplexType_example1.xml");
        String xsdFilePath = "src/test/resources/Testing_ChoiceComplexTypes.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_ChoiceComplexType.xml");
    }

    @Test
    public void transform_sequence() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/sequence_example1.xml");
        String xsdFilePath = "src/test/resources/TestingSequence.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_sequence.xml");
    }

    @Test
    public void transform_sequenceWithDuplicateEntry() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/sequence_exampleWithDuplicateEntry.xml");
        String xsdFilePath = "src/test/resources/TestingSequence.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_sequenceWithDuplicateEntry.xml");
    }

    @Test
    public void transform_extendedType() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/extendedType_example.xml");
        String xsdFilePath = "src/test/resources/Testing_ExtendedType.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_ExtendedType.xml");
    }

    @Test
    public void transform_minimum_requirements() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/minimum_requirements_example1.xml");
        String xsdFilePath = "src/test/resources/Testing_minimum_requirements.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

        check_output(outputStream, "src/test/resources/expected_populated_minimum_requirements.xml");
    }

    @Test
    public void transform_Requirements() throws IOException, SAXException {
        List<Long> times = new ArrayList<Long>();
        for (int i=0; i <1; i++) {
            long startTime = System.nanoTime();
            File xmlFile = new File("src/main/resources/Requirements.xml");
            String xsdFilePath = "src/main/resources/input/RequirementsSchemas.xsd";

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);

            try(FileWriter generatedFileWriter = new FileWriter("src/test/resources/generated_populated_requirements.xml")){
                generatedFileWriter.write(outputStream.toString());
            }

            check_output(outputStream, "src/test/resources/expected_populated_requirements.xml");
            long endTime = System.nanoTime() - startTime;
            times.add(endTime);
            System.out.println(i+"\t"+endTime);
        }
        times.forEach(System.out::println);
    }

    @Test
    public void transform_Requirement_with_hint() throws IOException, SAXException {
        File xmlFile = new File("src/test/resources/Testing_hint.xml");
        String xsdFilePath = "src/main/resources/input/RequirementsSchemas.xsd";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSDSchemaAndXMLToOntology.transform(xmlFile, xsdFilePath, outputStream);


        try(FileWriter generatedFileWriter = new FileWriter("src/test/resources/generated_populated_requirements_hint.xml")){
            generatedFileWriter.write(outputStream.toString());
        }

        check_output(outputStream, "src/test/resources/expected_testing_hint.xml");
    }

}