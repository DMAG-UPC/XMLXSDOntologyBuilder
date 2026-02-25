curl --location 'http://dmag1.ac.upc.edu/ds/sparql' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'query=PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema>
SELECT ?actorName ?rel ?requirementName
WHERE {
    {
        ?actor a foaf:Actor.
        BIND(foaf:Actor AS ?class).
    }
    UNION
    {
        ?actor a ?class.
        ?class <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Actor.
    }
    ?actor foaf:name ?actorName.
    ?requirement a ?requirement_type.
    ?requirement_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Requirement.
    ?requirement foaf:name ?requirementName.
    {
        ?requirement ?rel ?actor.
    } UNION {
        ?requirement ?rel2 ?tmp.
        ?tmp ?rel ?actor.
    }
}'