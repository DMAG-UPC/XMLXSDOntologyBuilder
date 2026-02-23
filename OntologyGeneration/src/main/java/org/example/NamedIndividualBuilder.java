package org.example;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlet.xsdparser.core.XsdParser;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedIndividualBuilder {
    final static String regex = "[\\n\\s]+";
    final static String subst = " ";
    final static Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    private static int counter = 0;

    private final int id = counter++;

    private final OWLDataFactory df = new OWLDataFactoryImpl();
    private final Map<String, List<Object>> data_properties = new LinkedHashMap<>();
    private final Map<String, List<NamedIndividualBuilder>> object_properties = new LinkedHashMap<>();

    private final Map<Object, List<Object>> structure = new LinkedHashMap<>();
    private final String namespace;
    private final String className;
    private String nameOfBuild;

    public String getNameOfBuild() {
        return nameOfBuild;
    }

    public void setNameOfBuild(String nameOfBuild) {
        this.nameOfBuild = nameOfBuild;
    }

    private String nameOfBuildWithNamespace;
    private OWLNamedIndividual owLNamedIndividual;
    private final Set<String> choiceNames;
    private String hintedName;

            /*"    <owl:NamedIndividual rdf:about=\"http://xmlns.com/foaf/0.1/test\">\n" +
                    "        <rdf:type rdf:resource=\"http://xmlns.com/foaf/0.1/rootType\"/>\n" +
                    "        <element_a>test</element_a>\n" +
                    "        <element_b rdf:datatype=\"http://www.w3.org/2001/XMLSchema#decimal\">1</element_b>\n" +
                    "    </owl:NamedIndividual>"*/

    public NamedIndividualBuilder(String namespace, String className, XsdParser parserInstance){
        this.namespace = namespace;
        this.className = className;
        this.choiceNames = new HashSet<>();
    }

    public NamedIndividualBuilder(String namespace, ElementPSVI elementToTranslate, XsdParser parserInstance) throws ParserConfigurationException, IOException, SAXException {
        this(namespace, elementToTranslate.getTypeDefinition().getName(), parserInstance);


        XSElementDeclaration elementToTranslateDeclaration = elementToTranslate.getElementDeclaration();

        if (!(elementToTranslateDeclaration.getTypeDefinition() instanceof XSComplexTypeDecl elementToTranslateTypeDeclaration)) {
            //Simple type
            return;
        }
        if (!(elementToTranslateTypeDeclaration.getParticle().getTerm() instanceof XSModelGroupImpl elementToTranslateModelGroupImpl)) {
            throw new NotImplementedException();
        }
        if (isChoice(elementToTranslateModelGroupImpl)) {
            handleChoice(namespace, (PSVIElementNSImpl) elementToTranslate, parserInstance, elementToTranslateModelGroupImpl);
        } else if (isSequence(elementToTranslateModelGroupImpl)){
            handleSequence(namespace, elementToTranslate, parserInstance, elementToTranslateModelGroupImpl);
        }
    }

    private static boolean isSequence(XSModelGroupImpl xsModelGroup) {
        return xsModelGroup.getCompositor() == XSModelGroup.COMPOSITOR_SEQUENCE;
    }

    private static boolean isChoice(XSModelGroupImpl xsModelGroup) {
        return xsModelGroup.getCompositor() == XSModelGroup.COMPOSITOR_CHOICE;
    }

    private void handleSequence(String namespace, ElementPSVI elementToTranslate, XsdParser parserInstance, XSModelGroupImpl elementToTranslateModelGroupImpl) throws ParserConfigurationException, IOException, SAXException {
        for (XSParticleDecl childParticle : elementToTranslateModelGroupImpl.fParticles) {
            String propertyName;
            propertyName = "";
            handleParticleOrModelGroup(namespace, (PSVIElementNSImpl) elementToTranslate, parserInstance, childParticle, propertyName, false);
        }
    }

    //keep
    private void handleParticleOrModelGroup(String namespace, PSVIElementNSImpl elementToTranslate, XsdParser parserInstance, XSParticleDecl childParticle, String propertyName, boolean b) throws ParserConfigurationException, IOException, SAXException {
        if (childParticle.fValue instanceof XSModelGroupImpl childModelGroupImpl) {
            if (childModelGroupImpl.getCompositor() == XSModelGroup.COMPOSITOR_CHOICE) {
                handleChoice(namespace, elementToTranslate, parserInstance, childModelGroupImpl);
            } else if (childModelGroupImpl.getCompositor() == XSModelGroup.COMPOSITOR_SEQUENCE) {
                for (var deFactorchildParticle : childModelGroupImpl.fParticles) {
                    handleParticleOrModelGroup(namespace, elementToTranslate, parserInstance, deFactorchildParticle, propertyName, b);
                }
            } else {
                throw new NotImplementedException();
            }
        } else {
            handleParticle(namespace, elementToTranslate, parserInstance, childParticle, propertyName, b);
        }
    }

    /*private static boolean isExtendingType(ElementPSVI psviElem) {
        return psviElem.getTypeDefinition().getBaseType() != null && !psviElem.getTypeDefinition().getBaseType().getNamespace().equals("http://www.w3.org/2001/XMLSchema");
    }

    private void handleSequenceParticle(String namespace, PSVIElementNSImpl psviElem, XsdParser parserInstance, XSParticleDecl childParticle, XSElementDecl xsElementDecl) throws ParserConfigurationException, IOException, SAXException {
        if (isIdentifierHintParticle(xsElementDecl)){
            defineHintedName(psviElem, xsElementDecl);
            return;
        }
        populateWithParticle(namespace, psviElem, childParticle, xsElementDecl, parserInstance, child_node);
    }*/

    private static boolean isIdentifierHintParticle(XSElementDecl xsElementDecl) {
        return xsElementDecl.getName().equalsIgnoreCase("identifier_hint");
    }

    private void defineHintedName(PSVIElementNSImpl psviElem, XSElementDecl xsElementDecl) {
        String childElementNamespace = xsElementDecl.fTargetNamespace;
        String childElementName = xsElementDecl.fName;

        var nodeList = getChildrenByTagNameNS(psviElem, childElementNamespace, childElementName);
        if (!nodeList.isEmpty()){
            setHintedName(nodeList.getFirst().getTextContent());
        }
    }

    //keep
    private void handleChoice(String namespace, PSVIElementNSImpl psviElem, XsdParser parserInstance, XSModelGroupImpl xsModelGroup) throws ParserConfigurationException, IOException, SAXException {
        var choiceName = getAppInfoValue(xsModelGroup);
        for (XSParticleDecl xsParticleDecl: xsModelGroup.fParticles) {
            populateWithParticle(namespace, psviElem, xsParticleDecl, (XSElementDecl) xsParticleDecl.fValue, parserInstance, choiceName);
        }
    }

    //keep
    private void handleParticle(String namespace, PSVIElementNSImpl fatherElement, XsdParser parserInstance, XSParticleDecl particleToHandle, String propertyName, boolean isChoice) throws ParserConfigurationException, IOException, SAXException {
        if (particleToHandle.fValue.getNamespace().equals("http://www.w3.org/2001/XMLSchema")
                && particleToHandle.fValue.getName().equals("string")
                && particleToHandle.fValue instanceof XSElementDecl
        ) {
            populateWithParticle(namespace, fatherElement, particleToHandle, (XSElementDecl) particleToHandle.fValue, parserInstance );
        } else if (particleToHandle.fValue instanceof XSElementDecl) {
            populateWithParticle(namespace, fatherElement, particleToHandle, (XSElementDecl) particleToHandle.fValue, parserInstance);
        } else if (particleToHandle.fValue instanceof XSModelGroupImpl) {
            var nodeList = getChildrenByTagNameNS(fatherElement, particleToHandle.fValue.getNamespace(), particleToHandle.fValue.getName());

            for (Node child_node : nodeList) {
                NamedIndividualBuilder namedIndividualBuilder = new NamedIndividualBuilder(
                        namespace,
                        (ElementPSVI) (child_node),
                        parserInstance
                );
                if (isChoice) {
                    addToChoiceProperty(propertyName);
                } else {
                    addObjectProperty(propertyName, namedIndividualBuilder);
                }
            }
        }
    }

    private void addToChoiceProperty(String choiceName) {
        choiceNames.add(choiceName);
    }

    private void populateWithParticle(String namespace, PSVIElementNSImpl psviElem, XSParticleDecl childParticle, XSElementDecl xsElementDecl, XsdParser parserInstance) throws ParserConfigurationException, IOException, SAXException {
        populateWithParticle(namespace, psviElem, childParticle, xsElementDecl, parserInstance, "");
    }


    private void populateWithParticle(String namespace, PSVIElementNSImpl psviElem, XSParticleDecl childSchemaParticle, XSElementDecl xsElementDecl, XsdParser parserInstance, String overrideName) throws ParserConfigurationException, IOException, SAXException {
        String childElementName = xsElementDecl.fName;
        if (childElementName.equals("identifier_hint")) {
            return;
        }

        String childElementNamespace = xsElementDecl.fTargetNamespace;
        String childElementTypeName = xsElementDecl.fType.getName();
        String childElementTypeNamespace = xsElementDecl.fType.getNamespace();

        if (childElementTypeNamespace.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema")) {
            if (childElementTypeName.equalsIgnoreCase("string")) {
                addStringDataProperty(psviElem, childElementNamespace, childElementName);
            } else if (childElementTypeName.equalsIgnoreCase("integer")) {
                addIntegerDataProperty(psviElem, childElementNamespace, childElementName);
            } else if (childElementTypeName.equalsIgnoreCase("boolean")) {
                addBooleanDataProperty(psviElem, childElementNamespace, childElementName);
            } else if (childElementTypeName.equalsIgnoreCase("anyURI")) {
                addStringDataProperty(psviElem, childElementNamespace, childElementName);
            } else if (childElementTypeName.equalsIgnoreCase("dateTime")) {
                addStringDataProperty(psviElem, childElementNamespace, childElementName);
            }
        } else if (childSchemaParticle.fValue instanceof XSElementDecl xsElementDecl1 && !
                xsElementDecl1.fType.getNamespace().equals("http://www.w3.org/2001/XMLSchema")) {

            if (xsElementDecl1.fType.getBaseType().getName().equals("string")){
                addDataProperty(psviElem, childElementNamespace, childElementName);
            } else {
                var children_of_type = getChildrenByTagNameNS(psviElem, childElementNamespace, childElementName);
                for (Node childXMLnode: children_of_type) {

                    NamedIndividualBuilder namedIndividualBuilder = new NamedIndividualBuilder(
                            namespace,
                            (ElementPSVI) childXMLnode,
                            parserInstance
                    );
                    if (overrideName.equalsIgnoreCase("")) {
                        addObjectProperty(childXMLnode.getLocalName(), namedIndividualBuilder);
                    } else {
                        addObjectProperty(overrideName, namedIndividualBuilder);
                    }
                }
            }
        }  else if (childSchemaParticle.fValue instanceof XSElementDecl xsElementDecl1 && !
                xsElementDecl1.fType.getNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
            populateWithChildrenOfType(namespace, psviElem, parserInstance, childElementNamespace, childElementName);
        } else if (childSchemaParticle.fValue instanceof XSModelGroupImpl xsModelGroup){
            //populateWithModelGroup(namespace, psviElem, childParticle, xsModelGroup, parserInstance);
        }
    }

    private void populateWithChildrenOfType(String namespace, PSVIElementNSImpl psviElem, XsdParser parserInstance, String childElementNamespace, String childElementName) throws ParserConfigurationException, IOException, SAXException {
        var children_of_type = getChildrenByTagNameNS(psviElem, childElementNamespace, childElementName);
        for (Node child_node: children_of_type) {
            NamedIndividualBuilder namedIndividualBuilder = new NamedIndividualBuilder(
                    namespace,
                    (ElementPSVI) child_node,
                    parserInstance
            );
            addObjectProperty(child_node.getLocalName(), namedIndividualBuilder);
        }
    }

    private void addBooleanDataProperty(PSVIElementNSImpl psviElem, String childElementNamespace, String childElementName) {
        var children_of_type = getChildrenByTagNameNS(psviElem, childElementNamespace, childElementName);
        for (Node child_node: children_of_type) {
            addDataPropertyValue(
                    childElementNamespace, childElementName,
                    Boolean.parseBoolean(child_node.getFirstChild().getNodeValue())
            );
        }
    }

    private void addIntegerDataProperty(PSVIElementNSImpl psviElem, String childElementNamespace, String childElementName) {
        var children_of_type = getChildrenByTagNameNS(psviElem, childElementNamespace, childElementName);
        for (Node child_node: children_of_type) {
            addDataPropertyValue(
                    childElementNamespace, childElementName,
                    Integer.parseInt(child_node.getFirstChild().getNodeValue())
            );
        }
    }

    private void addStringDataProperty(PSVIElementNSImpl psviElem, String childElementNamespace, String childElementName) {
        var children_of_type = getChildrenByTagNameNS(psviElem, childElementNamespace, childElementName);
        for (Node child_node: children_of_type) {
            if (child_node.getFirstChild() == null) {
                continue;
            }
            addDataPropertyValue(
                    childElementNamespace, child_node.getLocalName(),
                    child_node.getFirstChild().getNodeValue()
            );
        }
    }

    private static List<Node> getChildrenByTagNameNS(PSVIElementNSImpl psviElem, String childElementNamespace, String childElementName) {
        var tentativeResult = psviElem.getElementsByTagNameNS(childElementNamespace, childElementName);
        List<Node> result = new ArrayList<>();
        for (int i=0; i<tentativeResult.getLength(); i++){
            var tentativeNode = tentativeResult.item(i);
            if (tentativeNode.getParentNode() == psviElem){
                result.add(tentativeNode);
            }
        }
        return result;
    }

    private String getAppInfoValue(XSModelGroupImpl xsModelGroup) throws ParserConfigurationException, IOException, SAXException {
        var annotationString = xsModelGroup.getAnnotation().getAnnotationString();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(annotationString));
        Document document = builder.parse(is);
        return document.getDocumentElement().getElementsByTagName("xs:appinfo").item(0).getTextContent();
    }

    void addDataProperty(PSVIElementNSImpl xmlElement, String childElementNamespace, String childElementName){
        var children_of_type = getChildrenByTagNameNS(xmlElement, childElementNamespace, childElementName);
        for (Node child_node: children_of_type) {
            Object value = child_node.getFirstChild().getNodeValue();
            addDataPropertyValue(childElementNamespace, childElementName, value);
        }
    }

    void addDataPropertyValue(String childElementNamespace, String childElementName, Object value){
        data_properties.putIfAbsent(childElementName, new ArrayList<>());
        data_properties.get(childElementName).add(value);
        structure.putIfAbsent(childElementName, new ArrayList<>());
        structure.get(childElementName).add(value);
    }

    private void addObjectProperty(String name, NamedIndividualBuilder value){
        object_properties.putIfAbsent(name, new ArrayList<>());
        object_properties.get(name).add(value);
        structure.putIfAbsent(name, new ArrayList<>());
        structure.get(name).add(value.structure);
    }

    private AddAxiom buildDataPropertyInteger(OWLDataProperty property, OWLOntology ontology, OWLDataFactory df, OWLIndividual individual, int value){
        OWLLiteral literal = df.getOWLLiteral(String.valueOf(value), OWL2Datatype.XSD_INT);
        return new AddAxiom(ontology, df.getOWLDataPropertyAssertionAxiom(property, individual, literal));
    }

    private AddAxiom buildDataPropertyString(OWLDataProperty property, OWLOntology ontology, OWLDataFactory df, OWLIndividual individual, String value){
        OWLLiteral literal = df.getOWLLiteral(String.valueOf(value));
        return new AddAxiom(ontology, df.getOWLDataPropertyAssertionAxiom(property, individual, literal));
    }

    private AddAxiom buildDataPropertyBoolean(OWLDataProperty property, OWLOntology ontology, OWLDataFactory df, OWLIndividual individual, Boolean value){
        OWLLiteral literal = df.getOWLLiteral(value);
        return new AddAxiom(ontology, df.getOWLDataPropertyAssertionAxiom(property, individual, literal));
    }

    public void build(OWLOntology model, CreationContext creationContext){
        if (isStructureAlreadyKnown(creationContext)){
            setNameOfBuildWithNamespace(namespace+creationContext.getInstanceName(structure));
            setNameOfBuild(creationContext.getInstanceName(structure));
            return;
        }
        build_children(model, creationContext);
        String individualName = getName(creationContext);
        creationContext.addInstanceName(structure, individualName);

        OWLNamedIndividual individual = df.getOWLNamedIndividual(namespace+individualName);
        setNameOfBuildWithNamespace(namespace+individualName);
        setNameOfBuild(individualName);
        setOwLNamedIndividual(individual);

        List<OWLAxiomChange> axioms = new ArrayList<>();

        OWLClass clazz = df.getOWLClass(namespace+className);
        axioms.add(new AddAxiom(model, df.getOWLClassAssertionAxiom(clazz, individual)));


        processDataProperties(model, axioms, individual);
        processObjectProperties(model, axioms, individual);
        for(OWLAxiomChange axiomChange: axioms) {
            model.applyChanges(axiomChange);
        }

    }

    private void processObjectProperties(OWLOntology model, List<OWLAxiomChange> axioms, OWLNamedIndividual individual) {
        for(var objectPropertyMapEntry : object_properties.entrySet()){
            for(var objectPropertyEntry: objectPropertyMapEntry.getValue()) {
                OWLObjectPropertyExpression propertyExpression = new OWLObjectPropertyImpl(IRI.create(namespace, objectPropertyMapEntry.getKey()));
                OWLNamedIndividual to = objectPropertyEntry.getOwLNamedIndividual();
                axioms.add(buildObjectProperty(propertyExpression, individual, to, model, df));
            }
        }
    }

    private void processDataProperties(OWLOntology model, List<OWLAxiomChange> axioms, OWLNamedIndividual individual) {
        for(var dataPropertyMapEntry : data_properties.entrySet()){
            for (var dataPropertyEntry: dataPropertyMapEntry.getValue()) {
                var property = df.getOWLDataProperty(namespace + dataPropertyMapEntry.getKey());
                if (dataPropertyEntry instanceof Integer integerValue) {
                    axioms.add(buildDataPropertyInteger(property, model, df, individual, integerValue));
                } else if (dataPropertyEntry instanceof String stringValue) {
                    axioms.add(buildDataPropertyString(property, model, df, individual, stringValue));
                } else if (dataPropertyEntry instanceof Boolean booleanValue) {
                    axioms.add(buildDataPropertyBoolean(property, model, df, individual, booleanValue));
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private boolean isStructureAlreadyKnown(CreationContext creationContext) {
        return creationContext.getInstanceName(structure) != null;
    }

    private AddAxiom buildObjectProperty(OWLObjectPropertyExpression property, OWLIndividual from, OWLIndividual to, OWLOntology ontology, OWLDataFactory df){
        var axiom = df.getOWLObjectPropertyAssertionAxiom(property, from, to/*, annotations*/);
        return new AddAxiom(ontology, axiom);
    }

    private void build_children(OWLOntology model, CreationContext creationContext) {
        for (var mapEntry: object_properties.entrySet()){
            for (var entry: mapEntry.getValue()) {
                entry.build(model, creationContext);
            }
        }
    }

    private String getName(CreationContext creationContext) {
        if (creationContext.getInstanceName(structure)!=null){
            setNameOfBuild(creationContext.getInstanceName(structure));
            return creationContext.getInstanceName(structure);
        }
        String tentativeIndividualName = generateTentativeName(creationContext);

        if (creationContext.instanceNameIsUsed(tentativeIndividualName)){
            System.out.println("ALERT!!! name collision detected: " + tentativeIndividualName);
            tentativeIndividualName = solveCollision(creationContext, tentativeIndividualName);
        }

        tentativeIndividualName = urlFormatName(tentativeIndividualName);
        return tentativeIndividualName;
    }

    private String generateTentativeName(CreationContext creationContext) {
        String tentativeIndividualName = this.className.replaceAll("Type","")+"Instance";
        if (hintedName != null){
            tentativeIndividualName = this.className.replaceAll("Type","")+"_"+hintedName;
        } else if (data_properties.containsKey("name")){
            tentativeIndividualName = this.className.replaceAll("Type","")+"_"+data_properties.get("name").getFirst().toString();
        } else if (choiceNames.size() == 1){
            String choiceName = choiceNames.iterator().next();
            String choiceValueName = object_properties.get(choiceName).getFirst().getName(creationContext);
            tentativeIndividualName = choiceName+"_"+choiceValueName;
        } else if (!object_properties.isEmpty() || !data_properties.isEmpty()) {
            tentativeIndividualName = generateNameFromProperties();
        }else {
            System.out.println("ALERT!!! fallback instance name");
        }
        return tentativeIndividualName;
    }

    private static String solveCollision(CreationContext creationContext, String tentativeIndividualName) {
        int count = 0;
        while(creationContext.instanceNameIsUsed(tentativeIndividualName + count)){
            count++;
        }
        tentativeIndividualName += count;
        return tentativeIndividualName;
    }

    private static String urlFormatName(String tentativeIndividualName) {
        final Matcher matcher = pattern.matcher(tentativeIndividualName);
        tentativeIndividualName = matcher.replaceAll(subst);
        tentativeIndividualName = java.net.URLEncoder.encode(tentativeIndividualName, StandardCharsets.UTF_8);
        return tentativeIndividualName;
    }

    private String generateNameFromProperties() {
        String tentativeIndividualName;
        StringBuilder tentativeIndividualNameBuilder = new StringBuilder();
        tentativeIndividualNameBuilder.append(this.className.replaceAll("Type",""));

        for (var object_property_entry : object_properties.entrySet()){
            tentativeIndividualNameBuilder.append("__").append(object_property_entry.getKey());
            for (var entry : object_property_entry.getValue()) {
                tentativeIndividualNameBuilder.append("_").append(entry.getNameOfBuild());
            }
        }
        for (var data_property_entry : data_properties.entrySet()){
            tentativeIndividualNameBuilder.append("__").append(data_property_entry.getKey());
            for (var entry : data_property_entry.getValue()) {
                tentativeIndividualNameBuilder.append("_").append(entry.toString());
            }
        }
        tentativeIndividualName = tentativeIndividualNameBuilder.toString();
        return tentativeIndividualName;
    }

    public String getNameOfBuildWithNamespace() {
        if (nameOfBuildWithNamespace == null){
            throw new IllegalArgumentException();
        }
        return nameOfBuildWithNamespace;
    }

    public void setNameOfBuildWithNamespace(String nameOfBuildWithNamespace) {
        this.nameOfBuildWithNamespace = nameOfBuildWithNamespace;
    }

    public void setOwLNamedIndividual(OWLNamedIndividual owLNamedIndividual) {
        this.owLNamedIndividual = owLNamedIndividual;
    }

    public OWLNamedIndividual getOwLNamedIndividual() {
        if (owLNamedIndividual == null) {
            owLNamedIndividual = df.getOWLNamedIndividual(getNameOfBuildWithNamespace());

        }
        return owLNamedIndividual;
    }

    public void setHintedName(String hintedName) {
        this.hintedName = hintedName;
    }
}
