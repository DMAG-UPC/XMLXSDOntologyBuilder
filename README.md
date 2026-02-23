# Objective of the project
By providing an XML and XSD pair, this project tranform it into an Ontology (OWL/RDF).

# Building
The project can be built using the command `mvn package`.

# Usage
An ontology can be generated with this tool through the following command:
`java -jar target/XMLXSDOntologyBuilder-1.0-SNAPSHOT.jar generateOntology --xml path_to_xml.xml --xsd path_to_xsd.xsd --out path_to_ontology.xml`

For testing purposes, our ontology for IoMT can be used for testing:
'java -jar target/XMLXSDOntologyBuilder-1.0-SNAPSHOT.jar generateOntology --xml src/main/resources/Requirements.xml --xsd src/main/resources/input/RequirementsSchemas.xsd --out /mnt/c/Users/narow/Downloads/test_ontology.xml'

The provided schema should comply with the following rules:
- There shall be no nameless types. In XSD, an element can define its type in situ, without giving it a proper name. Nevertheless, as we need to define the ontology’s classes with their names, we rely on the XSD type’s name and mandate that it is always provided.
- XSD choices shall be annotated with a name. When creating the ontology’s Data or Object Property, we require a name for this relationship. As XSD’s choices are nameless, we mandate the provisioning of an annotation to provide their name.
- The root element shall be a list of elements of interest. The transformation program expects to find within the root element a sequence of elements to translate into ontology instances. Each element in the hierarchy is then traversed to discover every instance to be created in the ontology.

# SparQL queries on our IoMT requirements ontologies
## Retrieving Actor to Specification relations
```
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT ?actor (GROUP_CONCAT(DISTINCT ?specificationName; separator=", ") AS ?specificationNames)
WHERE {
    {
        ?requirement ?rel ?actor .
    } UNION {
        ?requirement ?rel ?mid1 .
        ?mid1 ?rel2 ?actor.
    } UNION {
        ?requirement ?rel ?mid1 .
        ?mid1 ?rel2 ?mid2 .
        ?mid2 ?rel3 ?actor.
    } UNION {
        ?mid1 ?rel2 ?mid2 .
        ?mid2 ?rel3 ?mid3 .
        ?mid3 ?rel4 ?actor.    
    }
    ?requirement a ?requirement_type.
    ?actor a foaf:Actor.
    ?requirement_type rdfs:subClassOf foaf:Requirement .
    ?requirement foaf:requirement_origin ?requirement_origin.
    ?requirement_origin foaf:specification ?specification.
    ?specification foaf:name ?specificationName.
}
GROUP BY ?actor
```

## Retrieving Event to Specification relations
```
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT ?event (GROUP_CONCAT(DISTINCT ?specificationName; separator=", ") AS ?specificationNames)
WHERE {
    {
        ?requirement ?rel ?event .
    } UNION {
        ?requirement ?rel ?mid1 .
        ?mid1 ?rel2 ?event.
    } UNION {
        ?requirement ?rel ?mid1 .
        ?mid1 ?rel2 ?mid2 .
        ?mid2 ?rel3 ?event.
    } UNION {
        ?mid1 ?rel2 ?mid2 .
        ?mid2 ?rel3 ?mid3 .
        ?mid3 ?rel4 ?event.    
    }
    ?requirement a ?requirement_type.
    ?event a foaf:Event.
    ?requirement_type rdfs:subClassOf foaf:Requirement .
    ?requirement foaf:requirement_origin ?requirement_origin.
    ?requirement_origin foaf:specification ?specification.
    ?specification foaf:name ?specificationName.
}
GROUP BY ?event
```

## Retrieving Component to Specification relations
```
PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT ?component (GROUP_CONCAT(DISTINCT ?specificationName; separator=", ") AS ?specificationNames)
WHERE {
    {
        ?requirement ?rel ?component .
    } UNION {
        ?requirement ?rel ?mid1 .
        ?mid1 ?rel2 ?component.
    } UNION {
        ?requirement ?rel ?mid1 .
        ?mid1 ?rel2 ?mid2 .
        ?mid2 ?rel3 ?component.
    } UNION {
        ?mid1 ?rel2 ?mid2 .
        ?mid2 ?rel3 ?mid3 .
        ?mid3 ?rel4 ?component.    
    }
    ?requirement a ?requirement_type.
    ?component a foaf:ComponentContent.
    ?requirement_type rdfs:subClassOf foaf:Requirement .
    ?requirement foaf:requirement_origin ?requirement_origin.
    ?requirement_origin foaf:specification ?specification.
    ?specification foaf:name ?specificationName.
}
GROUP BY ?component
```

## Retrieving Requirement to Specification Relation
```
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema>
SELECT ?requirement ?requirementName ?requirement_type ?specification ?modalVerb ?actorName WHERE {
   ?requirement a ?requirement_type ; foaf:requirement_origin ?origin ; foaf:name ?requirementName; foaf:actor ?actor.
   ?actor foaf:name ?actorName.
   ?origin foaf:specification ?specification ; foaf:clauseOrigin ?clauseOrigin.
   ?clauseOrigin foaf:modalVerb ?modalVerb.
   ?requirement_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Requirement .
 }
```

## Retrieving Invalidated requirements due to CVE
```
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema> 
SELECT ?specification ?modalVerb ?actorName ?CVE ?value ?cvegroup
WHERE {   
    ?requirement a ?requirement_type ; foaf:requirement_origin ?origin ; foaf:name ?requirementName; foaf:actor ?actor.   
    ?actor foaf:name ?actorName.
   ?origin foaf:specification ?specification ; foaf:clauseOrigin ?clauseOrigin.
   ?clauseOrigin foaf:modalVerb ?modalVerb.
   ?requirement_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Requirement .
   ?requirement foaf:invalidating_CVE_group ?cvegroup.
   ?cvegroup foaf:CVE ?CVE.
   ?CVE foaf:name ?CVEName
   FILTER regex(str(?CVEName), "CVE-2004-2261", "i")
}
```
