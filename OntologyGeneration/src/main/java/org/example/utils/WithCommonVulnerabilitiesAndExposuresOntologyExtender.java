package org.example.utils;

import com.google.common.base.Charsets;
import org.apache.xerces.stax.ImmutableLocation;
import org.apache.xerces.stax.events.AttributeImpl;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class WithCommonVulnerabilitiesAndExposuresOntologyExtender {
    public static void extend_ontology(
            InputStream inputOntologyStream,
            Set<String> CVEsOfInterest,
            Map<String, Set<String>> requirementToCVEGroups,
            Map<String, List<String>> type_to_CVEs,
            String outputPath
    ) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new InputStreamReader(inputOntologyStream));

        XMLOutputFactory factoryWriter = XMLOutputFactory.newInstance();
        XMLEventWriter writer = factoryWriter.createXMLEventWriter(new FileOutputStream(outputPath), Charsets.UTF_8.displayName());

        DecodingStatus decodingStatus = new DecodingStatus();
        StringBuilder requirementNameBuilder = new StringBuilder();

        Stack<Map<String, String>> attributes_stack = new Stack<>();
        // Iterate through the XML events
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            Map<String, String> attributes = new HashMap<>();

            switch (event.getEventType()) {
                case XMLStreamConstants.CHARACTERS:
                    processXMLStreamCharacters(decodingStatus.isInsideRequirementName(), requirementNameBuilder, event);
                    writer.add(event);
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    ProcessXMLStartEvent(event, attributes, attributes_stack, decodingStatus);
                    requirementNameBuilder = new StringBuilder();
                    writer.add(event);
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if (processXMLEndEvent(CVEsOfInterest, requirementToCVEGroups, type_to_CVEs, attributes_stack, event, decodingStatus, writer, requirementNameBuilder))
                        break;
                    writer.add(event);
                    break;

                default:
                    writer.add(event);
                    break;
            }
        }

        // Close the reader
        reader.close();
        writer.close();
    }

    public static void populateOntology(Set<String> CVEsOfInterest, Map<String, Set<String>> requirementToCVEGroups, Map<String, List<String>> type_to_CVEs, InputStream ontologyInputStream, String outputPath) throws IOException, XMLStreamException {
        extend_ontology(ontologyInputStream, CVEsOfInterest, requirementToCVEGroups, type_to_CVEs, outputPath);
    }

    private static void ProcessXMLStartEvent(XMLEvent event, Map<String, String> attributes, Stack<Map<String, String>> attributes_stack, DecodingStatus decodingStatus) {
        for (Iterator<Attribute> it = event.asStartElement().getAttributes(); it.hasNext(); ) {
            var attribute = it.next();
            attributes.put(attribute.getName().getLocalPart(), attribute.getValue());
        }
        attributes_stack.push(attributes);


        if (event.asStartElement().getName().getLocalPart().contains("NamedIndividual")) {
            decodingStatus.setInsideNamedIndividual(true);
        }
        if (decodingStatus.isInsideNamedIndividual()) {
            if (event.asStartElement().getName().getLocalPart().contains("type")) {
                if (
                        attributes.containsKey("resource") &&
                                attributes.get("resource").contains("Requirement") &&
                                !attributes.get("resource").contains("GeneralizedRequirement")
                ) {
                    decodingStatus.setInsideRequirement(true);
                }
            }
        }
        if (decodingStatus.isInsideRequirement()) {
            if (event.asStartElement().getName().getLocalPart().equals("name")) {
                decodingStatus.setInsideRequirementName(true);
            }
        }
    }

    private static boolean processXMLEndEvent(Set<String> CVEsOfInterest, Map<String, Set<String>> requirementToCVEGroups, Map<String, List<String>> type_to_CVEs, Stack<Map<String, String>> attributes_stack, XMLEvent event, DecodingStatus decodingStatus, XMLEventWriter writer, StringBuilder requirementNameBuilder) throws XMLStreamException {
        Map<String, String> attributes;
        attributes = attributes_stack.pop();

        if (event.asEndElement().getName().getLocalPart().contains("NamedIndividual")) {
            decodingStatus.setInsideNamedIndividual(false);
            decodingStatus.setInsideRequirement(false);
            if (!attributes.containsKey("about")){
                return true;
            }

            if (!decodingStatus.isHasWrittenCVEs()) {
                writer.add(event);
                write_new_CVEs(writer, CVEsOfInterest);
                decodingStatus.setHasWrittenCVEs(true);
                write_CVE_groups(writer, type_to_CVEs);
                return true;
            }

            //System.out.println("End Element: " + event.asEndElement().getName().getLocalPart());
        } else if (event.asEndElement().getName().getLocalPart().equals("name")) {
            if (decodingStatus.isInsideRequirementName()) {
                decodingStatus.setInsideRequirementName(false);
                writer.add(event);
                addInvalidatingCVEGroups(writer, requirementNameBuilder.toString(), requirementToCVEGroups);
                return true;
            }
        }
        return false;
    }

    private static void write_CVE_groups(XMLEventWriter writer, Map<String, List<String>> typeToCves) throws XMLStreamException {
        for (Map.Entry<String, List<String>> entry : typeToCves.entrySet()) {
            write_CVE_group(writer, entry.getKey(), entry.getValue());
        }
    }

    private static void write_CVE_group(XMLEventWriter writer, String key, List<String> value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new AttributeImpl(
                new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "about", "rdf"),
                "http://xmlns.com/foaf/0.1/CVE_Group_"+key,
                "CDATA",
                true,
                new ImmutableLocation(-1, -1, -1, null, null)
        ));

        writer.add(eventFactory.createCharacters("\n    "));
        writer.add(
                eventFactory.createStartElement(
                        new QName("http://www.w3.org/2002/07/owl#", "NamedIndividual", "owl"),
                        attributes.iterator(),
                        Collections.emptyIterator()
                )
        );

        create_CVEGroup_type(writer);
        createName(writer, key);
        writeCVEsOfGroup(writer, value);

        writer.add(eventFactory.createCharacters("\n    "));
        writer.add(
                eventFactory.createEndElement(
                        new QName("http://www.w3.org/2002/07/owl#", "NamedIndividual", "owl"),
                        Collections.emptyIterator()
                )
        );
    }

    private static void create_CVEGroup_type(XMLEventWriter writer) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new AttributeImpl(
                new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "resource", "rdf"),
                "http://xmlns.com/foaf/0.1/CVE_Group",
                "CDATA",
                true,
                new ImmutableLocation(-1, -1, -1, null, null)
        ));
        writer.add(eventFactory.createCharacters("\n    "));
        writer.add(
                eventFactory.createStartElement(
                        new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type", "rdf"),
                        attributes.iterator(),
                        Collections.emptyIterator()
                )
        );
        writer.add(
                eventFactory.createEndElement(
                        new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type", "rdf"),
                        Collections.emptyIterator()
                )
        );

    }

    private static void writeCVEsOfGroup(XMLEventWriter writer, List<String> CVEs) throws XMLStreamException {
        for(String cve : CVEs ) {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            List<Attribute> attributes = new ArrayList<>();

            attributes.add(new AttributeImpl(
                    new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "resource", "rdf"),
                    "http://xmlns.com/foaf/0.1/CVE_"+cve,
                    "CDATA",
                    true,
                    new ImmutableLocation(-1, -1, -1, null, null)
            ));

            writer.add(eventFactory.createCharacters("\n        "));
            writer.add(
                    eventFactory.createStartElement(
                            new QName("http://xmlns.com/foaf/0.1/", "CVE", ""),
                            attributes.iterator(),
                            Collections.emptyIterator()
                    )
            );
            writer.add(
                    eventFactory.createEndElement(
                            new QName("http://xmlns.com/foaf/0.1/", "CVE", ""),
                            Collections.emptyIterator()
                    )
            );
        }
    }


    private static void write_new_CVEs(XMLEventWriter writer, Set<String> CVEsOfInterest) throws XMLStreamException {
        for(String cve: CVEsOfInterest) {
            write_new_CVE(writer, cve);
        }
    }

    private static void write_new_CVE(XMLEventWriter writer, String cve) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new AttributeImpl(
                new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "about", "rdf"),
                "http://xmlns.com/foaf/0.1/CVE_"+cve,
                "CDATA",
                true,
                new ImmutableLocation(-1, -1, -1, null, null)
        ));

        writer.add(eventFactory.createCharacters("\n    "));
        writer.add(
                eventFactory.createStartElement(
                        new QName("http://www.w3.org/2002/07/owl#", "NamedIndividual", "owl"),
                        attributes.iterator(),
                        Collections.emptyIterator()
                )
        );

        createRDFTypeCVE(writer);
        createName(writer, cve);

        writer.add(eventFactory.createCharacters("\n    "));
        writer.add(
                eventFactory.createEndElement(
                        new QName("http://www.w3.org/2002/07/owl#", "NamedIndividual", "owl"),
                        Collections.emptyIterator()
                )
        );
        //writer.add();
    }

    private static void createRDFTypeCVE(XMLEventWriter writer) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new AttributeImpl(
                new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "resource", "rdf"),
                "http://xmlns.com/foaf/0.1/CVE",
                "CDATA",
                true,
                new ImmutableLocation(-1, -1, -1, null, null)
        ));

        writer.add(eventFactory.createCharacters("\n        "));
        writer.add(
                eventFactory.createStartElement(
                        new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type", "rdf"),
                        attributes.iterator(),
                        Collections.emptyIterator()
                )
        );
        writer.add(
                eventFactory.createEndElement(
                        new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "NamedIndividual", "rdf"),
                        Collections.emptyIterator()
                )
        );
    }

    private static void createName(XMLEventWriter writer, String cve) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        writer.add(eventFactory.createCharacters("\n        "));
        writer.add(
                eventFactory.createStartElement(
                        new QName("http://xmlns.com/foaf/0.1/}", "name", ""),
                        Collections.emptyIterator(),
                        Collections.emptyIterator()
                )
        );
        writer.add(eventFactory.createCharacters(cve));
        writer.add(
                eventFactory.createEndElement(
                        new QName("http://xmlns.com/foaf/0.1/}", "name", ""),
                        Collections.emptyIterator()
                )
        );
    }

    private static void addInvalidatingCVEGroups(
            XMLEventWriter writer,
            String requirementName,
            Map<String, Set<String>> requirementToCVEGroups
    ) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        if (!requirementToCVEGroups.containsKey(requirementName)) {
            System.out.println("unknown requirement: " + requirementName);
            return;
        }

        for (String cveGroup: requirementToCVEGroups.get(requirementName)) {
            List<Attribute> attributes = new ArrayList<>();

            attributes.add(new AttributeImpl(
                    new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "resource", "rdf"),
                    "http://xmlns.com/foaf/0.1/CVE_Group_"+cveGroup,
                    "CDATA",
                    true,
                    new ImmutableLocation(-1, -1, -1, null, null)
            ));

            writer.add(eventFactory.createCharacters("\n    "));
            writer.add(
                    eventFactory.createStartElement(
                            new QName("http://xmlns.com/foaf/0.1/", "invalidating_CVE_group", "foaf"),
                            attributes.iterator(),
                            Collections.emptyIterator()
                    )
            );
            writer.add(
                    eventFactory.createEndElement(
                            new QName("http://xmlns.com/foaf/0.1/", "invalidating_CVE_group", "foaf"),
                            Collections.emptyIterator()
                    )
            );
        }
    }

    private static void processXMLStreamCharacters(boolean inRequirementName, StringBuilder requirementNameBuilder, XMLEvent event) {
        if (inRequirementName) {
            requirementNameBuilder.append(event.asCharacters().getData());
        }
    }
}
