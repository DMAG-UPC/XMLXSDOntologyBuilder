package org.example;

import org.example.utils.WithSpyderiskExtender;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.example.NamedIndividualBuilderTest.check_output;

public class OntologiesRelationerTest {
    @Test
    public void addNewImportTest() throws IOException, OWLOntologyStorageException, OWLOntologyCreationException {
        IRI ontologyIRI = IRI.create("file:src/test/resources/expected_populated_requirements_with_dates.owl");
        var ontology = WithSpyderiskExtender.extendOntologyWithSpyderiskImport(ontologyIRI);
        try (FileOutputStream outputStream = new FileOutputStream("src/test/resources/generated_populated_requirements_with_Spyderisk_import.xml")){
            ontology.saveOntology(outputStream);
        }
    }

    @Test
    public void addNewTypesToNamedIndividuals() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException, SAXException {
        IRI ontologyIRI = IRI.create("file:src/test/resources/expected_populated_requirements_with_dates.owl");
        var ontology = WithSpyderiskExtender.extendOntologyWithSpyderiskTypes(ontologyIRI);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            ontology.saveOntology(outputStream);
            try(FileWriter generatedFileWriter = new FileWriter("src/test/resources/generated_populated_requirements_with_Spyderisk_types.xml")){
                generatedFileWriter.write(outputStream.toString());
            }
            check_output(outputStream, "src/test/resources/expected_populated_requirements_with_Spyderisk_types.xml");
        }
    }

    @Test
    public void addNewEquivalences() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException, SAXException {
        IRI ontologyIRI = IRI.create("file:src/test/resources/expected_populated_requirements_with_dates.owl");
        var ontology = WithSpyderiskExtender.extendOntologyWithSpyderiskEquivalences(ontologyIRI);


        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            ontology.saveOntology(outputStream);
            try(FileWriter generatedFileWriter = new FileWriter("src/test/resources/generated_populated_requirements_with_Spyderisk_equivalences.xml")){
                generatedFileWriter.write(outputStream.toString());
            }
            check_output(outputStream, "src/test/resources/expected_populated_requirements_with_Spyderisk_equivalences.xml");
        }
    }


    @Test
    public void addNewEquivalencesAndTypes() throws Exception {
        IRI ontologyIRI = IRI.create("file:src/test/resources/expected_populated_requirements_with_dates.owl"); // Modify the path
        var ontology = WithSpyderiskExtender.extendOntologyWithSpyderisk(ontologyIRI);


        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            ontology.saveOntology(outputStream);
            try(FileWriter generatedFileWriter = new FileWriter("src/test/resources/generated_populated_requirements_with_Spyderisk_equivalences_and_types.xml")){
                generatedFileWriter.write(outputStream.toString());
            }
            check_output(outputStream, "src/test/resources/expected_populated_requirements_with_Spyderisk_equivalences_and_types.xml");
        }
    }
}