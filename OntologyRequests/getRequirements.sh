curl --location 'http://147.83.30.144:3030/ds/sparql' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'query=PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema>
SELECT ?requirement ?requirementName ?specificationname ?modalVerb ?actorName
WHERE {
  ?requirement a ?requirement_type ; foaf:requirement_origin ?origin ; foaf:name ?requirementName; foaf:actor ?actor.
  ?actor foaf:name ?actorName.
  ?origin foaf:specification ?specification ; foaf:clauseOrigin ?clauseOrigin.
  ?specification foaf:name ?specificationname.
  ?clauseOrigin foaf:modalVerb ?modalVerb.
  ?requirement_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Requirement .
}'