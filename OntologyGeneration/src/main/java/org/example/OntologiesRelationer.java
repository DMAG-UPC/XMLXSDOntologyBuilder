package org.example;

import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Map;

public class OntologiesRelationer {
    public static OWLOntology addNewTypesToNamedIndividuals(OWLOntology ontology, OWLOntologyManager manager, Map<List<String>, List<String>> additions) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        for (Map.Entry<List<String>, List<String>> entry : additions.entrySet()) {
            for (String namedIndividualURI : entry.getKey()) {
                for (String newClassURI : entry.getValue()) {;
                    OWLClass newClass = factory.getOWLClass(newClassURI);

                    IRI individualIRI = IRI.create(namedIndividualURI);
                    OWLNamedIndividual individual = factory.getOWLNamedIndividual(individualIRI);

                    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(newClass, individual);

                    manager.addAxiom(ontology, classAssertion);
                }


            }
        }
        return ontology;
    }

    public static OWLOntology addEquivalences(OWLOntology ontology, OWLOntologyManager manager, Map<String, String> equivalences) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        for (Map.Entry<String, String> entry : equivalences.entrySet()) {
            OWLClass classA = factory.getOWLClass(IRI.create(entry.getKey()));
            OWLClass classB = factory.getOWLClass(IRI.create(entry.getValue()));

            OWLEquivalentClassesAxiom eqAxiom = factory.getOWLEquivalentClassesAxiom(classA, classB);

            manager.addAxiom(ontology, eqAxiom);
        }
        return ontology;
    }

    public static OWLOntology addImport(OWLOntology ontology, OWLOntologyManager manager, String importedUrl){

        // Step 3: Define the IRI of the ontology to be imported (the one containing the new types)
        IRI importOntologyIRI = IRI.create(importedUrl); // Modify with actual IRI

        // Step 4: Create the import declaration
        OWLImportsDeclaration importDeclaration = manager.getOWLDataFactory().getOWLImportsDeclaration(importOntologyIRI);

        // Step 5: Apply the import to the main ontology
        manager.applyChange(new AddImport(ontology, importDeclaration));

        return ontology;
    }
}
