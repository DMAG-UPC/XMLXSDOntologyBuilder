curl --location 'http://147.83.30.144:3030/ds/sparql' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'query=PREFIX foaf: <http://xmlns.com/foaf/0.1/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema> 
SELECT DISTINCT?name {
    ?requirement foaf:requirement_origin ?requirementOrigin.
    ?requirementOrigin foaf:specification ?specification.
    ?specification foaf:name ?name.
}'