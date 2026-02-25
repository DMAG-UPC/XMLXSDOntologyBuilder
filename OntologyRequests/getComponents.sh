curl --location 'http://dmag1.ac.upc.edu:3030/ds/sparql' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'query=PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema>
SELECT ?thing ?class ?name
WHERE {
    {
        ?thing a foaf:ComponentContent.
        BIND(foaf:ComponentContent AS ?class).
    }
    UNION
    {
        ?thing a ?class.
        ?class <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:ComponentContent.
    }
    OPTIONAL {
        ?thing foaf:name ?name.
    }
    ?requirement a ?requirement_type.
    ?requirement_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Requirement.
    ?requirement foaf:name ?requirementName.
    {
        ?requirement ?rel ?thing.
    } UNION {
        ?requirement ?rel2 ?tmp.
        ?tmp ?rel ?thing.
    }
}'