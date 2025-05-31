package org.example;

import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class elements {
        public static List<XsdComplexType> getComplexTypes(List<XsdAbstractElement> elements){
            List<XsdComplexType> result = new ArrayList<>();
            System.out.println(elements.size());
            for (XsdAbstractElement element: elements){
                if (element instanceof XsdElement) {
                    addComplexTypesFromElement((XsdElement) element, result);
                } else if (element instanceof XsdComplexType) {
                    addComplexTypesFromComplexType((XsdComplexType) element, result);
                }
            }
            return result;
        }

        private static void addComplexTypesFromElement(XsdElement element, List<XsdComplexType> result) {
            if (element.getXsdComplexType() != null){
                result.add(element.getXsdComplexType());
                addComplexTypesFromComplexType(element.getXsdComplexType(), result);
            }
        }

        private static void addComplexTypesFromComplexType(XsdComplexType xsdComplexType, List<XsdComplexType> result) {
            if (xsdComplexType.getXsdChildElement() != null){
                for(XsdAbstractElement abstractElement: xsdComplexType.getXsdChildElement().getXsdElements().toList()){
                    if (abstractElement instanceof XsdElement element) {
                        addComplexTypesFromElement(element, result);
                    } else if (abstractElement instanceof XsdChoice choiceElement){
                        for (XsdElement element: choiceElement.getChildrenElements().toList()){
                            addComplexTypesFromElement(element, result);
                        }
                    }
                }
            }
            if (xsdComplexType.getChildAsSequence() != null) {
                for (XsdElement element : xsdComplexType.getChildAsSequence().getChildrenElements().toList()) {
                    addComplexTypesFromElement(element, result);
                }
            }

            if (xsdComplexType.getChildAsChoice() != null) {
                for (XsdElement element : xsdComplexType.getChildAsChoice().getChildrenElements().toList()) {
                    addComplexTypesFromElement(element, result);
                }
            }
        }

        public static void main(String[] args) {
            String filePath = "src/test/resources/Test.xsd";
            XsdParser parserInstance1 = new XsdParser(filePath);

            List<XsdSchema> schemaList = parserInstance1.getResultXsdSchemas().toList();
            XsdSchema schema = schemaList.get(0);

            Stream<XsdAbstractElement> elementsStream = schema.getXsdElements();
            List<XsdComplexType> complexTypes = getComplexTypes(elementsStream.toList());
            System.out.println(complexTypes.size());

        }
    }
