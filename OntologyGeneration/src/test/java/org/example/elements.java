package org.example;

import org.example.ontology.*;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class elements {
   /*public static void ttmain(String [] args) {
       String filePath = "C:\\Users\\amand\\IdeaProjects\\jena_project\\src\\main\\resources\\TestingElementsRequirementsSchemas.xsd";
       XsdParser parserInstance2 = new XsdParser(filePath);
       Stream<XsdElement> elementsStream = parserInstance2.getResultXsdElements();
       List<XsdSchema> schemaList = parserInstance2.getResultXsdSchemas().toList();
       XsdSchema schema = schemaList.get(0);
       Stream<XsdElement> elemstre = schema.getChildrenElements();
       elemstre.forEach(ele ->{
           System.out.println(ele.getName());
       });
       elementsStream.forEach(element ->{
           System.out.println(element.getName());
       });
       XsdElement htmlElement = elementsStream.findFirst().get();
       XsdComplexType htmlComplexType = htmlElement.getXsdComplexType();

       //XsdAttribute manifestAttribute = htmlComplexType.getXsdAttributes().findFirst().get();
       XsdChoice choiceElement = htmlComplexType.getChildAsChoice();
       XsdGroup flowContentGroup = choiceElement.getChildrenGroups().findFirst().get();
       XsdAll flowContentAll = flowContentGroup.getChildAsAll();
       XsdElement elem1 = flowContentAll.getChildrenElements().findFirst().get();
   }*/

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
            //System.out.println(xsdComplexType.getName());
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

        public static void createhoice (List<XsdComplexType> complexTypes){
            ObjectPropertyType opt = new ObjectPropertyType();
            SubPropertyOfType sot = new SubPropertyOfType();
            DomainType dt, dt2 = new DomainType();
                ClassTypeOwl cto, cto2 = new ClassTypeOwl();
                UnionOfTypeOwl uoto, uoto2 = new UnionOfTypeOwl();
                RestrictionType rt, rt2 = new RestrictionType();
                OnPropertyType onpt, onpt2 = new OnPropertyType();
                SomeValuesFromType svft, svft2 = new SomeValuesFromType();
            RangeType rat = new RangeType();

        }

        public static void main(String[] args) {
            String filePath = "src/test/resources/Test.xsd";
            XsdParser parserInstance1 = new XsdParser(filePath);

            List<XsdSchema> schemaList = parserInstance1.getResultXsdSchemas().toList();
            XsdSchema schema = schemaList.get(0);

            //Stream<XsdElement> elementsStream = schema.getChildrenElements();
            Stream<XsdAbstractElement> elementsStream = schema.getXsdElements();
            List<XsdComplexType> complexTypes = getComplexTypes(elementsStream.toList());
            System.out.println(complexTypes.size());

        }
    }
