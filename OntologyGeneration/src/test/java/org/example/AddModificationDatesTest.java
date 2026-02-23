package org.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.example.XMLParserHelper.BlameAwareJaxbSaxHandler;
import org.example.XMLParserHelper.ElementCompleteHandler;
import org.example.XMLParserHelper.GitBlameService;
import org.example.requirements.Requirement;
import org.example.requirements.Requirements;
import org.example.requirements.Specification;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InaccessibleObjectException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AddModificationDatesTest {
    @Test
    public void addModificationDates() throws IOException, JAXBException, ParserConfigurationException, SAXException, DatatypeConfigurationException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.findGitDir();
        Assume.assumeTrue("Could not find git repository, skipping test", builder.getGitDir() != null);

        ElementCompleteHandler handler = (elementName, object, parent, start, end, blame, specificationDates) -> {
            System.out.println("working on element: " + elementName + " lines " + start + "-" + end);
            if (object instanceof Requirement req) {
                var last_commit = blame.stream()
                        .max(Comparator.comparingLong(a -> a.commit().getCommitTime()));
                last_commit.ifPresent(personIdent -> {
                    GregorianCalendar c = new GregorianCalendar();
                    c.setTime(personIdent.commit().getAuthorIdent().getWhen());
                    XMLGregorianCalendar date2;
                    try {
                        date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                        req.setLastModified(date2);
                    } catch (DatatypeConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (object instanceof Specification spec) {
                var last_commit = blame.stream()
                        .max(Comparator.comparingLong(a -> a.commit().getCommitTime()));
                last_commit.ifPresent(personIdent -> {
                    GregorianCalendar c = new GregorianCalendar();
                    c.setTime(personIdent.commit().getAuthorIdent().getWhen());
                    XMLGregorianCalendar date2;


                    try {
                        date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                        var currentDate = specificationDates.get(spec.getName());
                        if (currentDate == null || date2.toGregorianCalendar().after(currentDate.toGregorianCalendar())) {
                            specificationDates.put(spec.getName(), date2);
                        }
                    } catch (DatatypeConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };


        Repository repo = builder.build();

        GitBlameService blameService = new GitBlameService(repo);

        JAXBContext jaxbContext =
                JAXBContext.newInstance(Requirements.class);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        SAXParser saxParser = factory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        Map<String, XMLGregorianCalendar> specificationDates = new HashMap<>();

        BlameAwareJaxbSaxHandler blameAwareHandler = new BlameAwareJaxbSaxHandler(
                xmlReader,
                blameService,
                "OntologyGeneration/src/main/resources/Requirements.xml",
                handler,
                specificationDates
        );

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setListener(blameAwareHandler.getListener());

        xmlReader.setContentHandler(blameAwareHandler);
        blameAwareHandler.setContentHandler(unmarshaller.getUnmarshallerHandler());

        try (InputStream inputStream = new FileInputStream("src/main/resources/Requirements.xml")) {
            saxParser.parse(new InputSource(inputStream), (DefaultHandler) null);
        }
        Requirements reqs = (Requirements) unmarshaller.getUnmarshallerHandler().getResult();
        updateSpecificationsRecursively(reqs, specificationDates, new HashSet<>());


        var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "edu.upc.dmag ../../main/resources/input/RequirementsSchemas.xsd");
        marshaller.marshal(reqs, new File("src/test/resources/generated_requirements_with_dates.xml"));

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff;
        try (
                FileInputStream generatedFileInputStream = new FileInputStream("src/test/resources/generated_requirements_with_dates.xml");
                FileInputStream expectedFileInputStream = new FileInputStream("src/test/resources/expected_requirements_with_dates.xml")
        ) {
            diff = XMLUnit.compareXML(
                    new String(expectedFileInputStream.readAllBytes(), StandardCharsets.UTF_8),
                    new String(generatedFileInputStream.readAllBytes(), StandardCharsets.UTF_8)
            );
        }
        Assert.assertTrue(diff.similar());

        try (
                InputStream in = new FileInputStream("src/test/resources/expected_requirements_with_dates.xml");
                OutputStream out = new FileOutputStream("src/main/resources/requirements_with_dates.xml")
        ) {
            in.transferTo(out);
        }
    }

    private void updateSpecificationsRecursively(Object obj, Map<String, XMLGregorianCalendar> specificationDates, Set<Object> visited) {
        if (obj == null || !visited.add(obj)) {
            return;
        }

        if(obj instanceof Specification spec) {
            if (spec.getName() == null){
                System.out.println("specification with null name found" );
            }
            if (spec.getName() != null) {
                if(!specificationDates.containsKey(spec.getName())){
                    System.out.println("specification " + spec.getName() + " has no date found");
                } else {
                    spec.setLastUpdated(specificationDates.get(spec.getName()));
                    System.out.println("specification " + spec.getName() + " updated with date " + spec.getLastUpdated());
                }
            }
        }

        if (obj instanceof List<?> list) {
            for (Object item : list) {
                updateSpecificationsRecursively(item, specificationDates, visited);
            }
            return;
        }

        // Only inspect classes from our generated package
        /*if (!obj.getClass().getPackage().getName().startsWith("org.example.requirements")) {
            return;
        }*/

        Class<?> currentClass = obj.getClass();
        while (currentClass != null && currentClass != Object.class) {
            for (java.lang.reflect.Field field : currentClass.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    try {
                        Object fieldValue = field.get(obj);
                        updateSpecificationsRecursively(fieldValue, specificationDates, visited);
                    } catch (IllegalAccessException e) {
                        // Log or handle exception
                    }
                } catch (InaccessibleObjectException ignored) {
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }
}