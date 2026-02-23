package org.example.XMLParserHelper;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface ElementCompleteHandler {

    void onElementComplete(
            String elementName,
            Object jaxbObject,
            Object parent,
            int startLine,
            int endLine,
            List<BlameLine> blameLines,
            Map<String, XMLGregorianCalendar> specificationDates
    );
}
