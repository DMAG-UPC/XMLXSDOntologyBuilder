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
