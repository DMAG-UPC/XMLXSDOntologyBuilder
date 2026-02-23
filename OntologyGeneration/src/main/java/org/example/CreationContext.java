package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreationContext {
    private final Set<String> generatedNames = new HashSet<>();
    private final Map<Object, String> generatedIndividuals = new HashMap<>();

    public void addInstanceName(Object object, String instanceName){
        generatedIndividuals.put(object, instanceName);
        generatedNames.add(instanceName);
    }

    public boolean instanceNameIsUsed(String instanceName){
        return generatedNames.contains(instanceName);
    }

    public String getInstanceName(Object object){
        return generatedIndividuals.get(object);
    }
}
