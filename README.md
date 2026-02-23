# MedSecurance 🚀

This repository contains the tooling to transform a XML/XSD file pair into an OWL/RDF ontology. This tooling has been used to create an ontology of the requirements defined by specifications and standards in the world of Internet of Medical Things.

## How to use
This context contains four folders, to address different needs of the user.

### OntologyGeneration
This folder contains the Java project (to be compiled with Maven), which allows to translate the XML containing all requirements (and its XSD) to an ontology.

### OutputOntology
This folder contains the ontology in OWL/RDF format (i.e. the result of OntologyCreation when executed on all requirements).

This file can be opened in existing tools such as Protégé, or it can be made available in SPARQL as described in the next section.

### OntologyDocker
This folder contains a Dockerfile which makes the Ontology reachable over SPARQL (it starts a Jena/Fuseki, and translates the ontology to the required format)

In order to build the Docker image, enter the folder and execute the following command:

```
docker build -t ontologydocker .
```

This command alreay takes care of copying the ontology and translating it as required by Jena/Fuseki.

Once the docker image is build, the container should be started with:
```
docker run -p 3030:3030 ontologydocker
```

There is alternatively an existing docker image under: 'danielnaro/ontologyserver'.

It then offers a SPARQL endpoint on port 3030, which can be reached over `localhost`.


### OntologyRequests
This folder contains a collection of shell scripts, each one with one cURL command, showing examples of how to reach the Ontology over SPARQL.

An example of such cURL command follows:

```
curl --location 'http://localhost:3030/ds/sparql' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'query=PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema>
SELECT ?eventName ?event_type ?change_direction ?change_name ?change_target ?changedContentName ?identifiedContentName ?changeRelationship
WHERE {
    ?event a ?event_type; foaf:name ?eventName.
    ?event_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:GeneralEvent .
    OPTIONAL {
        ?event foaf:change ?changeInstance.
        ?changeInstance foaf:changed_content ?changedContentWrapper.
        ?changedContentWrapper foaf:Content_choice ?changedContent.
        ?changedContent foaf:name ?changedContentName.

        OPTIONAL {
            ?changeInstance foaf:change_direction ?change_direction.
        }
        OPTIONAL {
            ?changeInstance foaf:target ?change_target
        }
    }
    OPTIONAL {
        ?event foaf:identified ?identifiedContentWrapper.
        ?identifiedContentWrapper foaf:Content_choice ?identifiedContent.
        ?identifiedContent foaf:name ?identifiedContentName.
    }
}'
```

One can see the url (`http://localhost:3030/ds/sparql`), and the SPARQL query.
