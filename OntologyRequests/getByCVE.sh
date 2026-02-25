curl --location 'http://dmag1.ac.upc.edu:3030/ds/sparql' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'query=PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema> 
SELECT distinct ?specificationName ?modalVerb 
WHERE {   
    ?requirement a ?requirement_type ; foaf:requirement_origin ?origin ; foaf:name ?requirementName; foaf:actor ?actor.   
    ?actor foaf:name ?actorName.
   ?origin foaf:specification ?specification ; foaf:clauseOrigin ?clauseOrigin.
   ?clauseOrigin foaf:modalVerb ?modalVerb.
   ?requirement_type <http://www.w3.org/2000/01/rdf-schema#subClassOf> foaf:Requirement .
   ?requirement foaf:invalidating_CVE_group ?cvegroup.
   ?specification foaf:name ?specificationName.
   ?cvegroup foaf:CVE ?CVE.
   ?CVE foaf:name ?CVEName
   FILTER regex(str(?CVEName), "CVE-2004-2261", "i")
}'