package org.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.requirements.Requirement;
import org.example.requirements.RequirementOrigins;
import org.example.requirements.Requirements;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilteringRequirements {
    @Test
    public void testFilter() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Requirements.class);

        // Create Unmarshaller
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Unmarshal XML file to Java object
        Requirements requirementsRoot = (Requirements) unmarshaller.unmarshal(new File("src/main/resources/Requirements.xml"));

        // Get List of Employees
        List<Requirement> requirements = requirementsRoot.getDocumentationRequirementOrPublishRequirementOrEnsuringRequirement();
        List<Requirement> filteredRequirements = new ArrayList<>();
        for (Requirement requirement : requirements) {
            boolean is27001 = false;
            for (RequirementOrigins requirementOrigins : requirement.getRequirementOrigin()){
                if(requirementOrigins.getSpecification().getName().contains("MDCG")){
                    is27001 = true;
                    break;
                }
            }
            if (is27001){
                filteredRequirements.add(requirement);
            }
        }

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        Requirements newRequirementsRoot = new Requirements();
        newRequirementsRoot.getDocumentationRequirementOrPublishRequirementOrEnsuringRequirement().addAll(filteredRequirements);
        marshaller.marshal(newRequirementsRoot, System.out);
    }
}
