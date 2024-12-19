//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.02.08 a las 12:29:11 PM CET 
//


package org.example.ontology;

import javax.xml.namespace.QName;
import jakarta.annotation.Generated;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.example.requirementstordfs package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
public class ObjectFactory {

    private final static QName _Ontology_QNAME = new QName("http://www.w3.org/2002/07/owl#", "Ontology");
    private final static QName _Class_QNAME = new QName("http://www.w3.org/2002/07/owl#", "Class");
    private final static QName _OnProperty_QNAME = new QName("http://www.w3.org/2002/07/owl#", "onProperty");
    private final static QName _SomeValuesFrom_QNAME = new QName("http://www.w3.org/2002/07/owl#", "someValuesFrom");
    private final static QName _Cardinality_QNAME = new QName("http://www.w3.org/2002/07/owl#", "cardinality");
    private final static QName _AnnotationProperty_QNAME = new QName("http://www.w3.org/2002/07/owl#", "AnnotationProperty");
    private final static QName _Restriction_QNAME = new QName("http://www.w3.org/2002/07/owl#", "Restriction");
    private final static QName _ObjectProperty_QNAME = new QName("http://www.w3.org/2002/07/owl#", "ObjectProperty");
    private final static QName _ClassTypeOwlUnionOf_QNAME = new QName("http://www.w3.org/2002/07/owl#", "unionOf");
    private final static QName _Domain_QNAME = new QName("http://www.w3.org/2000/01/rdf-schema#", "domain");
    private final static QName _SubPropertyOf_QNAME = new QName("http://www.w3.org/2000/01/rdf-schema#", "subPropertyOf");
    private final static QName _Range_QNAME = new QName("http://www.w3.org/2000/01/rdf-schema#", "range");
    private final static QName _IsDefinedBy_QNAME = new QName("http://www.w3.org/2000/01/rdf-schema#", "isDefinedBy");
    private final static QName _Class_QNAME_RDFS = new QName("http://www.w3.org/2000/01/rdf-schema#", "Class"); //MIRAR DE CAMBIARLE EL NOMBRE
    private final static QName _ClassTypeSubClassOf_QNAME = new QName("http://www.w3.org/2000/01/rdf-schema#", "subClassOf");
    private final static QName _RDF_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF");
    private final static QName _Type_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type");
    private final static QName _Property_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "Property");
    private final static QName _Datatype_QNAME = new QName("http://www.w3.org/2000/01/rdf-schema#", "Datatype");
    private final static QName _OneOf_QNAME = new QName("http://www.w3.org/2002/07/owl#", "oneOf");
    private final static QName _DatatypeProperty_QNAME = new QName("http://www.w3.org/2002/07/owl#", "DatatypeProperty");
    private final static QName _First_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "first");
    private final static QName _Rest_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rest");
    private final static QName _Description_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "Description");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.requirementstordfs
     *
     */
    public ObjectFactory() {
    }
    public OntologyType createOntologyType() {
        return new OntologyType();
    }
    public ClassTypeOwl createClassTypeOwl() {
        return new ClassTypeOwl();
    }
    public OnPropertyType createOnPropertyType() {
        return new OnPropertyType();
    }
    public SomeValuesFromType createSomeValuesFromType() {
        return new SomeValuesFromType();
    }
    public CardinalityType createCardinalityType() {
        return new CardinalityType();
    }
    public AnnotationPropertyType createAnnotationPropertyType() {
        return new AnnotationPropertyType();
    }
    public ObjectPropertyType createObjectPropertyType(ObjectPropertyType objprop) {
        return new ObjectPropertyType();
    }
    public RestrictionType createRestrictionType() {
        return new RestrictionType();
    }
    public UnionOfTypeOwl createUnionOfTypeOwl() {
        return new UnionOfTypeOwl();
    }
    public DatatypeType createDatatypeType() {
        return new DatatypeType();
    }
    public OneOfType createOneOfType() {
        return new OneOfType();
    }
    public DatatypePropertyType createDatatypePropertyType() {
        return new DatatypePropertyType();
    }


    /**
     * Create an instance of {@link DomainType }
     *
     */
    public DomainType createDomainType() {
        return new DomainType();
    }

    /**
     * Create an instance of {@link SubPropertyOfType }
     *
     */
    public SubPropertyOfType createSubPropertyOfType() {
        return new SubPropertyOfType();
    }

    /**
     * Create an instance of {@link RangeType }
     *
     */
    public RangeType createRangeType() {
        return new RangeType();
    }

    /**
     * Create an instance of {@link IsDefinedByType }
     *
     */
    public IsDefinedByType createIsDefinedByType() {
        return new IsDefinedByType();
    }

    /**
     * Create an instance of {@link ClassType }
     *
     */
    public ClassType createClassType() {
        return new ClassType();
    }

    /**
     * Create an instance of {@link SubClassOfType }
     *
     */
    public SubClassOfType createSubClassOfType() {
        return new SubClassOfType();
    }

    /**
     * Create an instance of {@link RestrictionTypeRDFS }
     *
     */
    public RestrictionTypeRDFS createRestrictionTypeRDFS() {
        return new RestrictionTypeRDFS();
    }

    /**
     * Create an instance of {@link UnionOfType }
     *
     */
    public UnionOfType createUnionOfType() {
        return new UnionOfType();
    }

    public RDFType createRDFType() {
        return new RDFType();
    }

    public TypeType createTypeType() {
        return new TypeType();
    }

    public PropertyType createPropertyType() {
        return new PropertyType();
    }
    public FirstType createFirstType() {
        return new FirstType();
    }
    public RestType createRestType() {
        return new RestType();
    }
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "Ontology")
    public JAXBElement<OntologyType> createOntology(OntologyType value) {
        return new JAXBElement<OntologyType>(_Ontology_QNAME, OntologyType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "Class")
    public JAXBElement<ClassTypeOwl> createClass(ClassTypeOwl value) {
        return new JAXBElement<ClassTypeOwl>(_Class_QNAME, ClassTypeOwl.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "onProperty")
    public JAXBElement<OnPropertyType> createOnProperty(OnPropertyType value) {
        return new JAXBElement<OnPropertyType>(_OnProperty_QNAME, OnPropertyType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "someValuesFrom")
    public JAXBElement<SomeValuesFromType> createSomeValuesFrom(SomeValuesFromType value) {
        return new JAXBElement<SomeValuesFromType>(_SomeValuesFrom_QNAME, SomeValuesFromType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "cardinality")
    public JAXBElement<CardinalityType> createCardinality(CardinalityType value) {
        return new JAXBElement<CardinalityType>(_Cardinality_QNAME, CardinalityType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "AnnotationProperty")
    public JAXBElement<AnnotationPropertyType> createAnnotationProperty(AnnotationPropertyType value) {
        return new JAXBElement<AnnotationPropertyType>(_AnnotationProperty_QNAME, AnnotationPropertyType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "Restriction")
    public JAXBElement<RestrictionType> createRestriction(RestrictionType value) {
        return new JAXBElement<RestrictionType>(_Restriction_QNAME, RestrictionType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "domain")
    public JAXBElement<DomainType> createDomain(DomainType value) {
        return new JAXBElement<DomainType>(_Domain_QNAME, DomainType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "ObjectProperty")
    public JAXBElement<ObjectPropertyType> createObjectProperty(ObjectPropertyType value) {
        return new JAXBElement<ObjectPropertyType>(_ObjectProperty_QNAME, ObjectPropertyType.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "unionOf", scope = ClassTypeOwl.class)
    public JAXBElement<UnionOfTypeOwl> createClassTypeOwlUnionOf(UnionOfTypeOwl value) {
        return new JAXBElement<UnionOfTypeOwl>(_ClassTypeOwlUnionOf_QNAME, UnionOfTypeOwl.class, ClassTypeOwl.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubPropertyOfType }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubPropertyOfType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "subPropertyOf")
    public JAXBElement<SubPropertyOfType> createSubPropertyOf(SubPropertyOfType value) {
        return new JAXBElement<SubPropertyOfType>(_SubPropertyOf_QNAME, SubPropertyOfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RangeType }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RangeType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "range")
    public JAXBElement<RangeType> createRange(RangeType value) {
        return new JAXBElement<RangeType>(_Range_QNAME, RangeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsDefinedByType }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IsDefinedByType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "isDefinedBy")
    public JAXBElement<IsDefinedByType> createIsDefinedBy(IsDefinedByType value) {
        return new JAXBElement<IsDefinedByType>(_IsDefinedBy_QNAME, IsDefinedByType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClassType }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ClassType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "Class")
    public JAXBElement<ClassType> createClassRDFS(ClassType value) {
        return new JAXBElement<ClassType>(_Class_QNAME_RDFS, ClassType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsDefinedByType }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IsDefinedByType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "isDefinedBy", scope = ClassType.class)
    public JAXBElement<IsDefinedByType> createClassTypeIsDefinedBy(IsDefinedByType value) {
        return new JAXBElement<IsDefinedByType>(_IsDefinedBy_QNAME, IsDefinedByType.class, ClassType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubClassOfType }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubClassOfType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "subClassOf", scope = ClassType.class)
    public JAXBElement<SubClassOfType> createClassTypeSubClassOf(SubClassOfType value) {
        return new JAXBElement<SubClassOfType>(_ClassTypeSubClassOf_QNAME, SubClassOfType.class, ClassType.class, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "RDF")
    public JAXBElement<RDFType> createRDF(RDFType value) {
        return new JAXBElement<RDFType>(_RDF_QNAME, RDFType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "type")
    public JAXBElement<TypeType> createType(TypeType value) {
        return new JAXBElement<TypeType>(_Type_QNAME, TypeType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "Property")
    public JAXBElement<PropertyType> createProperty(PropertyType value) {
        return new JAXBElement<PropertyType>(_Property_QNAME, PropertyType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/2000/01/rdf-schema#", name = "Datatype")
    public JAXBElement<DatatypeType> createDatatype(DatatypeType value) {
        return new JAXBElement<DatatypeType>(_Datatype_QNAME, DatatypeType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "oneOf")
    public JAXBElement<OneOfType> createOneOf(OneOfType value) {
        return new JAXBElement<OneOfType>(_OneOf_QNAME, OneOfType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/2002/07/owl#", name = "DatatypeProperty")
    public JAXBElement<DatatypePropertyType> createDatatypeProperty(DatatypePropertyType value) {
        return new JAXBElement<DatatypePropertyType>(_DatatypeProperty_QNAME, DatatypePropertyType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "first")
    public JAXBElement<FirstType> createFirst(FirstType value) {
        return new JAXBElement<FirstType>(_First_QNAME, FirstType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "rest")
    public JAXBElement<RestType> createRest(RestType value) {
        return new JAXBElement<RestType>(_Rest_QNAME, RestType.class, null, value);
    }
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "Description")
    public JAXBElement<DescriptionType> createDescription(DescriptionType value) {
        return new JAXBElement<DescriptionType>(_Description_QNAME, DescriptionType.class, null, value);
    }

}
