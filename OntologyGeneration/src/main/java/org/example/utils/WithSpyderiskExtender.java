package org.example.utils;

import org.example.OntologiesRelationer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WithSpyderiskExtender {

    public static OWLOntology extendOntologyWithSpyderisk(IRI inputOntologyIRI) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyIRI);

        return extendOntologyWithSpyderisk(
                ontology,
                manager
        );
    }

    public static OWLOntology extendOntologyWithSpyderisk(
            OWLOntology ontology,
            OWLOntologyManager manager
    ) {
        extendOntologyWithSpyderiskImport(ontology, manager);
        extendOntologyWithSpyderiskEquivalences(ontology, manager);
        return extendOntologyWithSpyderiskTypes(ontology, manager);

    }

    public static OWLOntology extendOntologyWithSpyderiskImport(IRI inputOntologyIRI) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyIRI);

        return extendOntologyWithSpyderiskImport(
                ontology,
                manager
        );
    }

    public static OWLOntology extendOntologyWithSpyderiskImport(
            OWLOntology ontology,
            OWLOntologyManager manager
    ) {
        return OntologiesRelationer.addImport(
                ontology,
                manager,
                "http://danielnaro.github.io/StaticOntologies/merged.rdf"
        );
    }

    public static OWLOntology extendOntologyWithSpyderiskTypes(IRI inputOntologyIRI) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyIRI);

        extendOntologyWithSpyderiskImport(ontology, manager);
        return extendOntologyWithSpyderiskTypes(ontology, manager);
    }

    public static OWLOntology extendOntologyWithSpyderiskTypes(OWLOntology ontology, OWLOntologyManager manager) {
        Map<List<String>, List<String>> newTypes = new LinkedHashMap<>();
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_sensor"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Sensor"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_sensitive+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#SensitiveData"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_server"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Server"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Data"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/GeneralContent_asset","http://xmlns.com/foaf/0.1/ComponentContent_CIoT+asset","http://xmlns.com/foaf/0.1/ComponentContent_existing+asset"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/core#Asset"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_interface","http://xmlns.com/foaf/0.1/ComponentContent_software+interface"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Interface"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/action_using+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataUse"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_stored+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#StoredDataPool"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/action_updating+collected+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataUpdate"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/Actor_stakeholder"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Stakeholder"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/action_backing+up+custom+data+on+each+device","http://xmlns.com/foaf/0.1/action_backing+up+data","http://xmlns.com/foaf/0.1/action_backing+up"), List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataCopy"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/action_accessing+collected+patient+personal+data","http://xmlns.com/foaf/0.1/action_accessing+patient+personal+healthcare+data","http://xmlns.com/foaf/0.1/action_accessing+personal+information+gathered+by+device","http://xmlns.com/foaf/0.1/action_accessing+restricted+resource","http://xmlns.com/foaf/0.1/action_accessing+secure+material+on+device","http://xmlns.com/foaf/0.1/action_accessing+to+collected+user+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataAccess"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_remote+access"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#RemoteAccessService"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/GeneralContent_medical+it-network","http://xmlns.com/foaf/0.1/ComponentContent_network"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#NetworkAsset"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_software","http://xmlns.com/foaf/0.1/ComponentContent_Medical+Deivce+Software","http://xmlns.com/foaf/0.1/ComponentContent_boot+software","http://xmlns.com/foaf/0.1/ComponentContent_device+software","http://xmlns.com/foaf/0.1/ComponentContent_external+software","http://xmlns.com/foaf/0.1/ComponentContent_health+software","http://xmlns.com/foaf/0.1/ComponentContent_operating+system+software"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#GeneralProcess"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_data+authenticator","http://xmlns.com/foaf/0.1/ComponentContent_multi-factor+authenticator"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#AuthService"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_service"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Process"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/GeneralContent_risk+control","http://xmlns.com/foaf/0.1/ComponentContent_access+control","http://xmlns.com/foaf/0.1/ComponentContent_security+control","http://xmlns.com/foaf/0.1/ComponentContent_implemented+control","http://xmlns.com/foaf/0.1/ComponentContent_device+security+control","http://xmlns.com/foaf/0.1/GeneralContent_control"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#ControlProcess"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/action_information+processing"), List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataProcess"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_mobile+client"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#MobileClient"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_desktop+client"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Workstation"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_data+in+transit"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataExchange"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_secure+communication+channel"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#DataChannel"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_patient+personal+healthcare+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+patient+personal+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+user+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#HealthData"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_patient+personal+healthcare+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+patient+personal+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+user+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#BiometricData"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_collected+patient+personal+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+user+data","http://xmlns.com/foaf/0.1/ComponentContent_user+data","http://xmlns.com/foaf/0.1/ComponentContent_data","http://xmlns.com/foaf/0.1/ComponentContent_personal+data","http://xmlns.com/foaf/0.1/ComponentContent_patient+personal+healthcare+data","http://xmlns.com/foaf/0.1/ComponentContent_private+data","http://xmlns.com/foaf/0.1/ComponentContent_patient+data","http://xmlns.com/foaf/0.1/ComponentContent_critical+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+data","http://xmlns.com/foaf/0.1/ComponentContent_encrypted+data","http://xmlns.com/foaf/0.1/ComponentContent_sensitive+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#GDPRArt9Data"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/ComponentContent_collected+patient+personal+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+user+data","http://xmlns.com/foaf/0.1/ComponentContent_collected+data"),List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#SensedData"));
        newTypes.put(List.of("http://xmlns.com/foaf/0.1/Actor_organization"), List.of("http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Organisation"));

        return OntologiesRelationer.addNewTypesToNamedIndividuals(
                ontology,
                manager,
                newTypes
        );
    }

    public static OWLOntology extendOntologyWithSpyderiskEquivalences(IRI inputOntologyIRI) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyIRI);

        extendOntologyWithSpyderiskImport(ontology, manager);
        return extendOntologyWithSpyderiskEquivalences(ontology, manager);
    }

    private static OWLOntology extendOntologyWithSpyderiskEquivalences(OWLOntology ontology, OWLOntologyManager manager) {
        Map<String, String> newEquivalences = new LinkedHashMap<>();
        newEquivalences.put("http://xmlns.com/foaf/0.1/Actor", "http://it-innovation.soton.ac.uk/ontologies/trustworthiness/domain#Human");
        newEquivalences.put("http://xmlns.com/foaf/0.1/Threat", "http://ontology.spyderisk.org/ns/core#Threat");
        newEquivalences.put("http://xmlns.com/foaf/0.1/Role", "http://it-innovation.soton.ac.uk/ontologies/trustworthiness/core#Role");
        newEquivalences.put("http://xmlns.com/foaf/0.1/ControlOver", "http://it-innovation.soton.ac.uk/ontologies/trustworthiness/core#Control");


        return OntologiesRelationer.addEquivalences(
                ontology,
                manager,
                newEquivalences
        );
    }
}
