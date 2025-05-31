package org.example;

import org.apache.xerces.xs.ElementPSVI;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NamedIndividualBuilderTest {

    @Test
    public void buildFromXMLWithOneElement() {
        File xmlFile = new File("src/test/resources/oneComplexType_example1.xml");
        //File xsdFile = new File("src/test/resources/Testing_OneComplexType.xsd");
        String modelPath = "src/test/resources/generated_rdf_OneComplexType.xml";
        CreationContext creationContext = new CreationContext();

        final OWLOntologyManager manager = OWLManager.createConcurrentOWLOntologyManager();
        try {
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(modelPath));

            Document doc = XSDSchemaAndXMLToOntology.getDocument(xmlFile);

            //ElementPSVI documentElement = (ElementPSVI)doc.getDocumentElement();
            Element documentElement = doc.getDocumentElement();
            NodeList nodeList = documentElement.getChildNodes();
            for(int i=0; i<nodeList.getLength(); i++){
                if (nodeList.item(i) instanceof ElementPSVI element){
                    NamedIndividualBuilder namedIndividualBuilder = new NamedIndividualBuilder("http://xmlns.com/foaf/0.1/", element, null);
                    namedIndividualBuilder.build(ontology, creationContext);
                };
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ontology.saveOntology(outputStream);
            outputStream.close();


            check_output(outputStream, "src/test/resources/expected_populated_rdf_OneComplexType.xml");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void buildFromXMLWithTwoElement() {
        File xmlFile = new File("src/test/resources/oneComplexType_example2.xml");
        //File xsdFile = new File("src/test/resources/Testing_OneComplexType.xsd");
        String modelPath = "src/test/resources/generated_rdf_OneComplexType.xml";
        CreationContext creationContext = new CreationContext();

        final OWLOntologyManager manager = OWLManager.createConcurrentOWLOntologyManager();
        try {
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(modelPath));

            Document doc = XSDSchemaAndXMLToOntology.getDocument(xmlFile);

            //ElementPSVI documentElement = (ElementPSVI)doc.getDocumentElement();
            Element documentElement = doc.getDocumentElement();
            NodeList nodeList = documentElement.getChildNodes();
            for(int i=0; i<nodeList.getLength(); i++){
                if (nodeList.item(i) instanceof ElementPSVI element){
                    NamedIndividualBuilder namedIndividualBuilder = new NamedIndividualBuilder("http://xmlns.com/foaf/0.1/", element, null);
                    namedIndividualBuilder.build(ontology, creationContext);
                };
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ontology.saveOntology(outputStream);
            outputStream.close();


            check_output(outputStream, "src/test/resources/expected_populated_rdf_OneComplexTypeTwoEntries.xml");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    static void check_output(ByteArrayOutputStream outputStream, String expected_result_path) throws IOException, SAXException {
        try (FileInputStream fileInputStream = new FileInputStream(expected_result_path)) {
            XMLUnit.setIgnoreWhitespace(true);
            XMLUnit.setIgnoreComments(true);
            XMLUnit.setIgnoreAttributeOrder(true);
            Diff diff = XMLUnit.compareXML(
                    new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    outputStream.toString(StandardCharsets.UTF_8)
            );
            Assert.assertTrue(diff.similar());
        }
    }
}