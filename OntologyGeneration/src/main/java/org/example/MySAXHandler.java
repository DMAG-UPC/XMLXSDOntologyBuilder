package org.example;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MySAXHandler extends DefaultHandler {


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        System.out.println("start: "+uri+"\t"+localName+"\t"+qName+"\t");
        for(int i=0; i<attributes.getLength(); i++){
            System.out.println(attributes.getType(i)+" "+attributes.getValue(i));
        }
        System.out.println(attributes.getValue("type"));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("end: "+uri+"\t"+localName+"\t"+qName+"\t");
    }

    /*@Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }*/
}
