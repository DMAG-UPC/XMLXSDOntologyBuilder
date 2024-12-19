package org.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.example.ontology.*;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.*;
import org.xmlet.xsdparser.xsdelements.elementswrapper.NamedConcreteElement;
import org.xmlet.xsdparser.xsdelements.elementswrapper.ReferenceBase;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;

import javax.xml.namespace.QName;
import java.io.OutputStream;
import java.util.*;

import static org.example.ComplexTypesExtractor.getComplexTypes;

public class XSDSchemaToOntologySchema {

    public static class RangeAndDescriptionContainer {
        private final RangeType range;
        private final List<DescriptionType> desc_list;

        public RangeAndDescriptionContainer() {
            range = new RangeType();
            desc_list = new ArrayList<>();
        }

        public RangeType getRange() {
            return range;
        }

        public List<DescriptionType> getDescriptionList() {
            return desc_list;
        }
    }

    static final QName aboutQName = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "about");
    static final QName resourceQName = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "resource");
    static final QName labelQName = new QName("http://www.w3.org/2000/01/rdf-schema#", "label");
    static final QName commentQName = new QName("http://www.w3.org/2000/01/rdf-schema#", "comment");
    static final QName term_statusQName = new QName("http://www.w3.org/2003/06/sw-vocab-status/ns#", "term_status");
    static final QName titleQName = new QName("http://purl.org/dc/elements/1.1/", "title");
    static final QName descriptionQName = new QName("http://purl.org/dc/elements/1.1/", "description");
    static final QName parseTypeQName = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "parseType");


    public static void calling_marshall(ObjectFactory objectFactory, RDFType root, OutputStream stream){
        try {
            JAXBContext context = JAXBContext.newInstance(RDFType.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(objectFactory.createRDF(root), stream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    public static void calling_marshall(ObjectFactory objectFactory, RDFType root){
        calling_marshall(objectFactory, root, System.out);
    }
    public static void initialize_ontology(ObjectFactory objectFactory, RDFType root){

        OntologyType ontology = new OntologyType();
        ontology.getOtherAttributes().put(aboutQName, "http://xmlns.com/foaf/0.1/");
        ontology.getOtherAttributes().put(titleQName, "Friend of a Friend (FOAF) vocabulary");
        ontology.getOtherAttributes().put(descriptionQName, "The Friend of a Friend (FOAF) RDF vocabulary, described using W3C RDF Schema and the Web Ontology Language.");
        addToRDF(root, objectFactory.createOntology(ontology));
        List<String> uris = Arrays.asList(
                "http://xmlns.com/wot/0.1/assurance",
                "http://xmlns.com/wot/0.1/src_assurance",
                "http://www.w3.org/2003/06/sw-vocab-status/ns#term_status",
                "http://purl.org/dc/elements/1.1/description",
                "http://purl.org/dc/elements/1.1/title",
                "http://purl.org/dc/elements/1.1/date"
        );
        for (String uri : uris) {
            AnnotationPropertyType annot = new AnnotationPropertyType();
            annot.getOtherAttributes().put(aboutQName, uri);
            addToRDF(root, objectFactory.createAnnotationProperty(annot));
        }
        ClassTypeOwl owlclass = new ClassTypeOwl();
        owlclass.getOtherAttributes().put(aboutQName, "http://www.w3.org/2000/01/rdf-schema#Class");
        addToRDF(root, objectFactory.createClass(owlclass));

    }

    private static void processElement(ObjectFactory objectFactory, RDFType root, XsdAbstractElement element) {
        if (element instanceof XsdComplexType complexType) {
            processComplexType(objectFactory, root, complexType);
        }
    }

    private static void processComplexType(ObjectFactory objectFactory, RDFType root, XsdComplexType complexType) {
        Map<String, String> attributesMap = complexType.getAttributesMap();
        attributesMap.forEach((key, value) -> {
            ClassType classType = new ClassType();
            classType.getOtherAttributes().put(aboutQName, "http://xmlns.com/foaf/0.1/" + value);
            classType.getOtherAttributes().put(labelQName, value);
            TypeType type = new TypeType();
            classType.getContent().add(objectFactory.createType(type));
            type.getOtherAttributes().put(resourceQName, "http://www.w3.org/2002/07/owl#Class");

            XsdComplexContent complexContent = complexType.getComplexContent();

            if (complexContent != null && complexContent.getXsdExtension().getBase() != null) {
                handleExtensionRelationship(classType, complexContent);
            } else {
                populateDefinedBy(classType);
            }
            addToRDF(root, objectFactory.createClassRDFS(classType));
        });
    }

    private static void handleExtensionRelationship(ClassType classType, XsdComplexContent complexContent) {
        SubClassOfType subClass = new SubClassOfType();
        JAXBElement<SubClassOfType> subClassElement = new JAXBElement<>(new QName("http://www.w3.org/2000/01/rdf-schema#", "subClassOf"), SubClassOfType.class, subClass);
        classType.getContent().add(subClassElement);
        ReferenceBase baseElement = ReferenceBase.createFromXsd(complexContent.getXsdExtension().getBase());
        NamedConcreteElement namedConcreteElement = (NamedConcreteElement) baseElement;
        String nameValue = namedConcreteElement.getElement().getAttributesMap().get("name");
        subClass.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + nameValue);
    }

    private static void populateDefinedBy(ClassType classType) {
        IsDefinedByType isDefinedBy = new IsDefinedByType();
        JAXBElement<IsDefinedByType> isDefinedByElement = new JAXBElement<>(new QName("http://www.w3.org/2000/01/rdf-schema#", "isDefinedBy"), IsDefinedByType.class, isDefinedBy);
        classType.getContent().add(isDefinedByElement);
        isDefinedBy.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/");
    }

    public static void listPropertiesOfChoice(XsdAbstractElement element, String clave, ObjectFactory objectFactory, RDFType root) {
        var choice_name = ((XsdChoice) element).getAnnotation().getAppInfoList().getFirst().getContent();

        ObjectPropertyType objectprop = new ObjectPropertyType();
        SubPropertyOfType subpropertyof = new SubPropertyOfType();
        DomainType domain = new DomainType();
        RangeType range = new RangeType();
        ClassTypeOwl classowl = new ClassTypeOwl();
        UnionOfTypeOwl unionof = new UnionOfTypeOwl();
        OnPropertyType onproperty = new OnPropertyType();

        objectprop.getOtherAttributes().put(aboutQName, "http://xmlns.com/foaf/0.1/" + choice_name);
        subpropertyof.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + choice_name);
        domain.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + clave);
        unionof.getOtherAttributes().put(parseTypeQName, "Collection");
        onproperty.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + choice_name);

        List<XsdAbstractElement> choice_elements = element.getXsdElements().toList();

        for (XsdAbstractElement abst_elem : choice_elements) {
            process_choice_entry(abst_elem, onproperty, unionof);

        }

        JAXBElement<UnionOfTypeOwl> unionoftypeelement = new JAXBElement<>(new QName("http://www.w3.org/2002/07/owl#", "unionOf"), UnionOfTypeOwl.class, unionof);
        classowl.getContent().add(unionoftypeelement);

        JAXBElement<ClassTypeOwl> owlclasselement = new JAXBElement<>(new QName("http://www.w3.org/2002/07/owl#", "Class"), ClassTypeOwl.class, classowl);
        range.getContent().add(owlclasselement);

        objectprop.getAny().add(objectFactory.createSubPropertyOf(subpropertyof));
        objectprop.getAny().add(objectFactory.createDomain(domain));
        objectprop.getAny().add(objectFactory.createRange(range));

        addToRDF(root, objectFactory.createObjectProperty(objectprop));
    }

    private static void process_choice_entry(XsdAbstractElement abst_elem, OnPropertyType onproperty, UnionOfTypeOwl unionof) {
        SomeValuesFromType somevalues = new SomeValuesFromType();
        String type = extractStringMethod(abst_elem.getAttributesMap().get("type"), ':');
        addTypeToSomeValues(type, somevalues);

        RestrictionType restrict = new RestrictionType();
        restrict.setOnProperty(
                new JAXBElement<>(new QName("http://www.w3.org/2002/07/owl#", "onProperty"), OnPropertyType.class, onproperty)
        );
        restrict.setSomeValuesFrom(
                new JAXBElement<>(new QName("http://www.w3.org/2002/07/owl#", "someValuesFrom"), SomeValuesFromType.class, somevalues)
        );

        unionof.getRestriction().add(new JAXBElement<>(new QName("http://www.w3.org/2002/07/owl#", "Restriction"), RestrictionType.class, restrict));
    }

    private static void addTypeToSomeValues(String type, SomeValuesFromType somevalues) {
        if (type.equals("string")) {
            somevalues.getOtherAttributes().put(resourceQName, "http://www.w3.org/2000/01/rdf-schema#Literal");
        } else if (type.equals("boolean")) {
            somevalues.getOtherAttributes().put(resourceQName, "http://www.w3.org/2001/XMLSchema#boolean");
        } else {
            somevalues.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + type);
        }
    }

    private static String extractTypeMethod(String names_type) {
        int index = names_type.indexOf(":");
        return names_type.substring(index + 1);
    }
    private static String extractStringMethod(String names_type, char index_value) {
        int index;
        if (index_value == ':') {
            index = names_type.indexOf(":");
        } else if (index_value == '/') {
            index = names_type.lastIndexOf("/");
        } else if (index_value == '#') {
            index = names_type.lastIndexOf("#");
        } else {
            return null;
        }
        return names_type.substring(index + 1);
    }


    public static void listPropertiesOfSimpleTypeElement(XsdElement xsdelem, String clave, ObjectFactory objectFactory, RDFType root) {

        DatatypePropertyType dataprop = new DatatypePropertyType();
        DomainType domain = new DomainType();
        RangeType range = new RangeType();
        DatatypeType datatype = new DatatypeType();
        OneOfType oneof = new OneOfType();
        IsDefinedByType definedby = new IsDefinedByType();

        dataprop.getOtherAttributes().put(aboutQName, "http://xmlns.com/foaf/0.1/" + xsdelem.getName());
        dataprop.getOtherAttributes().put(term_statusQName, "testing");
        dataprop.getOtherAttributes().put(labelQName, xsdelem.getName());
        dataprop.getOtherAttributes().put(commentQName, xsdelem.getName());
        domain.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + clave);
        List<XsdEnumeration> enumer = xsdelem.getXsdSimpleType().getRestriction().getEnumeration();

        TypeType type = new TypeType();
        type.getOtherAttributes().put(resourceQName, "http://www.w3.org/1999/02/22-rdf-syntax-ns#List");
        String last_value = enumer.getLast().getValue();
        FirstType last_first = new FirstType();
        last_first.setValue(last_value);
        RestType last_rest = new RestType();
        last_rest.getOtherAttributes().put(resourceQName, "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");
        DescriptionType desc = new DescriptionType();

        desc.setType(objectFactory.createType(type));
        desc.setFirst(objectFactory.createFirst(last_first));
        desc.setRest(objectFactory.createRest(last_rest));

        DescriptionType descrip = null;
        for (int i = enumer.size() - 2; i >= 0; i--) {
            XsdEnumeration enume = enumer.get(i);
            RestType rest = new RestType();
            if (i == enumer.size() - 2) {
                rest.setDescription(objectFactory.createDescription(desc));
            } else {
                rest.setDescription(objectFactory.createDescription(descrip));
            }

            FirstType first = new FirstType();
            String enum_value = enume.getValue();
            first.setValue(enum_value);

            descrip = new DescriptionType();
            descrip.setType(objectFactory.createType(type));
            descrip.setFirst(objectFactory.createFirst(first));
            descrip.setRest(objectFactory.createRest(rest));
        }
        oneof.getDescription().add(objectFactory.createDescription(descrip));
        datatype.setAny(objectFactory.createOneOf(oneof));
        range.getContent().add(objectFactory.createDatatype(datatype));
        definedby.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/");

        dataprop.getAny().add(objectFactory.createDomain(domain));
        dataprop.getAny().add(objectFactory.createRange(range));
        dataprop.getAny().add(objectFactory.createIsDefinedBy(definedby));
        addToRDF(root, objectFactory.createDatatypeProperty(dataprop));
    }
    public static Map<String, PropertyType> listPropertiesOfElement(XsdAbstractElement element, String clave, ObjectFactory objectFactory) {

        PropertyType prop = new PropertyType();
        TypeType type = new TypeType();
        DomainType domain = new DomainType();
        RangeType range = new RangeType();
        IsDefinedByType definedby = new IsDefinedByType();

        prop.getOtherAttributes().put(aboutQName, "http://xmlns.com/foaf/0.1/" + element.getAttributesMap().get("name"));
        prop.getOtherAttributes().put(term_statusQName, "testing");
        prop.getOtherAttributes().put(labelQName, element.getAttributesMap().get("name"));
        prop.getOtherAttributes().put(commentQName, element.getAttributesMap().get("name"));
        String names_type = element.getAttributesMap().get("type");
        String type_s = extractStringMethod(names_type, ':');
        if (type_s.equals("string")) {
            type.getOtherAttributes().put(resourceQName, "http://www.w3.org/2002/07/owl#DatatypeProperty");
            range.getOtherAttributes().put(resourceQName, "http://www.w3.org/2000/01/rdf-schema#Literal");
        } else if (type_s.equals("boolean")) {
            type.getOtherAttributes().put(resourceQName, "http://www.w3.org/2002/07/owl#DatatypeProperty");
            range.getOtherAttributes().put(resourceQName, "http://www.w3.org/2001/XMLSchema#boolean");
        } else if (type_s.equals("integer") || type_s.equals("int")){
            type.getOtherAttributes().put(resourceQName, "http://www.w3.org/2002/07/owl#DatatypeProperty");
            range.getOtherAttributes().put(resourceQName, "http://www.w3.org/2001/XMLSchema#int");
        } else {
            type.getOtherAttributes().put(resourceQName, "http://www.w3.org/2002/07/owl#ObjectProperty");
            range.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + type_s);
        }
        domain.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/" + clave);
        definedby.getOtherAttributes().put(resourceQName, "http://xmlns.com/foaf/0.1/");
        prop.getAny().add(objectFactory.createType(type));
        prop.getAny().add(objectFactory.createDomain(domain));
        prop.getAny().add(objectFactory.createRange(range));
        prop.getAny().add(objectFactory.createIsDefinedBy(definedby));
        Map<String, PropertyType> result = new HashMap<>();
        result.put(element.getAttributesMap().get("name"), prop);
        return result;
    }

    public static RangeAndDescriptionContainer creating_description (List<PropertyType> prop_value){
        String range_value = null;
        boolean sameRangeValue = true;
        RangeAndDescriptionContainer container = new RangeAndDescriptionContainer();

        for (PropertyType property : prop_value) {
            for (Object obj : property.getAny()) {
                if (obj instanceof JAXBElement<?>) {
                    JAXBElement<?> jaxbElement = (JAXBElement<?>) obj;
                    Object innerObj = jaxbElement.getValue();
                    if (innerObj instanceof DomainType domain_obj) {
                        String dom_value = domain_obj.getOtherAttributes().values().iterator().next();
                        DescriptionType desc = new DescriptionType();
                        desc.getOtherAttributes().put(aboutQName, dom_value);
                        container.getDescriptionList().add(desc);
                    }
                    if (innerObj instanceof RangeType range_obj) {
                        String ran_value = range_obj.getOtherAttributes().values().iterator().next();
                        String currentRangeValue = extractStringMethod(ran_value, '#');
                        if (range_value == null) {
                            range_value = currentRangeValue;
                        } else if (!range_value.equals(currentRangeValue)) {
                            sameRangeValue = false;
                        }
                    }
                }
            }
            if (sameRangeValue == true) {
                if (range_value.equals("Literal")) {
                    container.getRange().getOtherAttributes().put(resourceQName, "http://www.w3.org/2000/01/rdf-schema#Literal");
                } else if (range_value.equals("boolean")) {
                    container.getRange().getOtherAttributes().put(resourceQName, "http://www.w3.org/2001/XMLSchema#boolean");
                } else if (range_value.equals("int")) {
                    container.getRange().getOtherAttributes().put(resourceQName, "http://www.w3.org/2001/XMLSchema#int");
                } else {
                    container.getRange().getOtherAttributes().put(resourceQName, range_value);
                }
            }
        }
        return container;
    }

    public static void domain_union(List<PropertyType> propertyDefinitions, String propertyName, ObjectFactory objectFactory, RDFType root){
        RangeAndDescriptionContainer propertyRangeAndDescription = creating_description(propertyDefinitions);
        RangeType type_range = propertyRangeAndDescription.getRange();
        List<DescriptionType> desc = creating_description(propertyDefinitions).getDescriptionList();

        if (
            isDataProperty(type_range)
        ) {
            DatatypePropertyType dataprop = new DatatypePropertyType();
            creating_data_or_object_property(propertyName, objectFactory, type_range, desc, dataprop.getOtherAttributes(), dataprop.getAny());
            addToRDF(root, objectFactory.createDatatypeProperty(dataprop));
        } else{
            ObjectPropertyType objprop = new ObjectPropertyType();
            creating_data_or_object_property(propertyName, objectFactory, type_range, desc, objprop.getOtherAttributes(), objprop.getAny());
            addToRDF(root, objectFactory.createObjectProperty(objprop));
        }
    }

    private static boolean isDataProperty(RangeType type_range) {
        return type_range.getOtherAttributes().values().iterator().next().equals("http://www.w3.org/2000/01/rdf-schema#Literal") ||
                type_range.getOtherAttributes().values().iterator().next().equals("http://www.w3.org/2001/XMLSchema#boolean") ||
                type_range.getOtherAttributes().values().iterator().next().equals("http://www.w3.org/2001/XMLSchema#int");
    }

    private static void creating_data_or_object_property(String prop_clave, ObjectFactory objectFactory, RangeType type_range, List<DescriptionType> desc, Map<QName, String> otherAttributes, List<Object> any) {
        DomainType domain = new DomainType();
        ClassTypeOwl class_owl = new ClassTypeOwl();
        UnionOfTypeOwl unionof = new UnionOfTypeOwl();
        otherAttributes.put(aboutQName, "http://xmlns.com/foaf/0.1/" + prop_clave);
        otherAttributes.put(term_statusQName, "testing");
        otherAttributes.put(labelQName, prop_clave);
        otherAttributes.put(commentQName, prop_clave);
        desc.forEach(description -> unionof.getAny().add(objectFactory.createDescription(description)));
        unionof.getOtherAttributes().put(parseTypeQName, "Collection");
        class_owl.getContent().add(objectFactory.createClassTypeOwlUnionOf(unionof));
        domain.getContent().add(objectFactory.createClass(class_owl));
        any.add(objectFactory.createDomain(domain));
        any.add(objectFactory.createRange(type_range));
    }

    public static void transform(String pathOfXsdFile, OutputStream outputStream){
        XsdParser parserInstance1 = new XsdParser(pathOfXsdFile);
        ObjectFactory objectFactory = new ObjectFactory();
        RDFType root = createRootWithSchemaLocations(objectFactory);

        XsdSchema schema = parserInstance1.getResultXsdSchemas().toList().getFirst();
        initialize_ontology(objectFactory, root);

        Map<String, XsdComplexType> complexTypes = getComplexTypes(schema);
        createClassesInOntology(complexTypes, objectFactory, root);

        Map<String, List<PropertyType>> propertyNameToPropertyDefinition = getProperties(complexTypes, objectFactory, root);

        for (Map.Entry<String, List<PropertyType>> entry : propertyNameToPropertyDefinition.entrySet()) {
            String propertyName = entry.getKey();
            List<PropertyType> propertyDefinitions = entry.getValue();


            if (propertyDefinitions.size() == 1) {
                PropertyType prop = propertyDefinitions.getFirst();
                addToRDF(root, objectFactory.createProperty(prop));
            } else if (!propertyDefinitions.isEmpty()){
                domain_union(propertyDefinitions, propertyName, objectFactory, root);
            }
        }
        calling_marshall(objectFactory, root, outputStream);
    }

    private static void addToRDF(RDFType root, JAXBElement property) {
        root.getAny().add(property);
    }

    private static Map<String, List<PropertyType>> getProperties(Map<String, XsdComplexType> complexTypes, ObjectFactory objectFactory, RDFType root) {
        Map<String, List<PropertyType>> properties_list = new HashMap<>();
        for (Map.Entry<String, XsdComplexType> entry : complexTypes.entrySet()) {
            if (entry.getKey() == null){
                continue;
            }
            properties_list = listPropertiesOfComplexType(entry, objectFactory, root, properties_list);
        }
        return properties_list;
    }

    private static void createClassesInOntology(Map<String, XsdComplexType> complexTypes, ObjectFactory objectFactory, RDFType root) {
        complexTypes.forEach((key, value) -> {
            processComplexType(objectFactory, root, value);
        });
    }

    private static Map<String, List<PropertyType>> listPropertiesOfComplexType(Map.Entry<String, XsdComplexType> entry, ObjectFactory objectFactory, RDFType root, Map<String, List<PropertyType>> domain_prop) {
        String key = entry.getKey();
        XsdComplexType complexType = entry.getValue();

        XsdAbstractElement xsdAbstractElement = complexType.getXsdChildElement();
        if (xsdAbstractElement == null) {
            //It is an extending complex type
            xsdAbstractElement = complexType.getComplexContent().getXsdExtension().getXsdChildElement();
        }
        return listPropertiesOfAbstractElement(objectFactory, root, key, complexType, xsdAbstractElement, domain_prop);
    }

    private static Map<String, List<PropertyType>> listPropertiesOfAbstractElement(ObjectFactory objectFactory, RDFType root, String key, XsdComplexType complexType, XsdAbstractElement xsdAbstractElement, Map<String, List<PropertyType>> domain_prop) {
        var name = complexType.getName();
        List<PropertyType> properties = new ArrayList<>();
        if (domain_prop.containsKey(name)) {
            properties = domain_prop.get(name);
        }

        domain_prop.put(name, properties);

        if (xsdAbstractElement == null) {
            return domain_prop;
        }
        switch (xsdAbstractElement) {
            case XsdSequence sequence -> sequence.getXsdElements().filter(it -> !isHint(it)).forEach(
                    element -> listPropertiesOfAbstractElement(objectFactory, root,  key, complexType, element, domain_prop)
            );
            case XsdChoice choice -> listPropertiesOfChoice(choice, key, objectFactory, root);
            case XsdElement xsdElem -> {
                listPropertiesOfElement(objectFactory, root, key, xsdAbstractElement, domain_prop, xsdElem);
            }
            default -> {
            }
        }
        return domain_prop;
    }

    private static void listPropertiesOfElement(ObjectFactory objectFactory, RDFType root, String key, XsdAbstractElement xsdAbstractElement, Map<String, List<PropertyType>> domain_prop, XsdElement xsdElem) {
        if (xsdElem.getXsdSimpleType() != null) {
            listPropertiesOfSimpleTypeElement(xsdElem, key, objectFactory, root);
        } else {
            listPropertiesOfComplexTypeElement(objectFactory, key, xsdAbstractElement, domain_prop);
        }
    }

    private static void listPropertiesOfComplexTypeElement(ObjectFactory objectFactory, String key, XsdAbstractElement xsdAbstractElement, Map<String, List<PropertyType>> domain_prop) {
        Map<String, PropertyType> res = listPropertiesOfElement(xsdAbstractElement, key, objectFactory);
        String claveRes = res.keySet().iterator().next();
        if (domain_prop.containsKey(claveRes)) {
            List<PropertyType> listProperties = domain_prop.get(claveRes);
            PropertyType valorRes = res.get(claveRes);
            listProperties.add(valorRes);
            domain_prop.put(claveRes, listProperties);
        } else {
            domain_prop.put(claveRes, new ArrayList<>(res.values()));
        }
    }

    private static boolean isHint(XsdAbstractElement it) {
        if (it instanceof XsdElement xsdElement){
            return xsdElement.getName().equalsIgnoreCase("identifier_hint");
        }
        return false;
    }

    private static RDFType createRootWithSchemaLocations(ObjectFactory objectFactory) {
        RDFType rdfType = objectFactory.createRDFType();
        rdfType.getOtherAttributes().put(
                new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"),
                "http://purl.org/dc/elements/1.1/ https://danielnaro.github.io/OWL_RDF_schemas/Ontology_2_dc.xsd " +
                    " http://www.w3.org/2003/06/sw-vocab-status/ns# https://danielnaro.github.io/OWL_RDF_schemas/Ontology_2_rdf.xsd"
        );
        return rdfType;
    }
}