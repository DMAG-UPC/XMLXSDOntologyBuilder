package org.example;

import org.xmlet.xsdparser.xsdelements.XsdComplexType;
import org.xmlet.xsdparser.xsdelements.XsdSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComplexTypesExtractor {
    public static Map<String, XsdComplexType> getComplexTypes(XsdSchema schema) {
        return schema.getChildrenComplexTypes().collect(Collectors.toMap(XsdComplexType::getName, Function.identity(),
                (o1, o2) -> o1, HashMap::new));
    }
}