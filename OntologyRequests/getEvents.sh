curl --location 'http://dmag1.ac.upc.edu:3030/ds/sparql' \
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