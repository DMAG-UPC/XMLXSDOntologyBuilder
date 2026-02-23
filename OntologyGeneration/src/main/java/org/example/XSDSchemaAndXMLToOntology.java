package org.example;

import org.apache.xerces.xs.ElementPSVI;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlet.xsdparser.core.XsdParser;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class XSDSchemaAndXMLToOntology {

    public static void transform(String xmlFilePath, String xsdFilePath, OutputStream finalResultoutputStream) throws IOException {
        File tempFile = File.createTempFile("generatedOntologySchema-", ".xml");
        tempFile.deleteOnExit();

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)){
            XSDSchemaToOntologySchema.transform(xsdFilePath, outputStream);
        }

        XsdParser parserInstance = new XsdParser(xsdFilePath);
        final OWLOntologyManager manager = OWLManager.createConcurrentOWLOntologyManager();

        CreationContext creationContext = new CreationContext();
        try {
            final OWLOntology ontology = manager.loadOntologyFromOntologyDocument(tempFile);

            Document doc = getDocument(new File(xmlFilePath));

            Element documentElement = doc.getDocumentElement();
            NodeList nodeList = documentElement.getChildNodes();
            List<NamedIndividualBuilder> builderList = new ArrayList<>();
            for(int i=0; i<nodeList.getLength(); i++){
                if (nodeList.item(i) instanceof ElementPSVI element){
                    builderList.add(new NamedIndividualBuilder(
                            "http://xmlns.com/foaf/0.1/",
                            element,
                            parserInstance
                    ));
                };
            }
            builderList.forEach(it -> it.build(ontology, creationContext));

            ontology.saveOntology(finalResultoutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document getDocument(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        // DOMSource
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);
        dbf.setAttribute("http://apache.org/xml/features/validation/schema", Boolean.TRUE);
        dbf.setAttribute(
                "http://apache.org/xml/properties/dom/document-class-name",
                "org.apache.xerces.dom.PSVIDocumentImpl"
        );
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.getSchema();

        return db.parse(xmlFile);
    }

    public static boolean validateDom(File xmlFile, File xsdFile) throws IOException, SAXException, ParserConfigurationException {
        //SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsdFile);

        Validator validator = schema.newValidator();

        // DOMSource
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(xmlFile);
        DOMSource source = new DOMSource(doc);
        //source.setSystemId(systemId);
        try{
            validator.validate(source);
            return true;
        }catch (Exception e){
            System.out.println(xmlFile.getName() + " is not valid because ");
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean validateFile(File xmlFile, File xsdFile) throws SAXException, IOException
    {
        // 1. Lookup a factory for the W3C XML Schema language
        //SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // 2. Compile the schema.
        Schema schema = factory.newSchema(xsdFile);

        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();

        // 4. Parse the document you want to check.
        Source source = new StreamSource(xmlFile);

        // 5. Check the document
        try
        {
            validator.validate(source);
            return true;
        }
        catch (SAXException ex)
        {
            System.out.println(xmlFile.getName() + " is not valid because ");
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
