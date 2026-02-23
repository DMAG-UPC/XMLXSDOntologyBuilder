package org.example.XMLParserHelper;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

import jakarta.xml.bind.Unmarshaller;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

public class BlameAwareJaxbSaxHandler extends XMLFilterImpl {

    private final GitBlameService blameService;
    private final ElementCompleteHandler callback;
    private final String xmlPath;

    private Locator locator;
    private final Deque<ElementContext> stack = new ArrayDeque<>();
    private final Map<String, XMLGregorianCalendar> specificationDates;

    public BlameAwareJaxbSaxHandler(
            XMLReader xmlReader,
            GitBlameService blameService,
            String xmlPath,
            ElementCompleteHandler callback,
            Map<String, XMLGregorianCalendar> specificationDates
    ) {
        super(xmlReader);
        this.blameService = blameService;
        this.xmlPath = xmlPath;
        this.callback = callback;
        this.specificationDates = specificationDates;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }

    @Override
    public void startElement(
            String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        stack.push(new ElementContext(qName, locator.getLineNumber()));
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(
            String uri, String localName, String qName)
            throws SAXException {

        super.endElement(uri, localName, qName);

        ElementContext ctx = stack.pop();
        int endLine = locator.getLineNumber();

        try {
            List<BlameLine> blame =
                    blameService.blameRange(xmlPath, ctx.startLine, endLine);

            callback.onElementComplete(
                    ctx.qName,
                    ctx.jaxbObject,
                    ctx.parent,
                    ctx.startLine,
                    endLine,
                    blame,
                    specificationDates
            );

        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    public Unmarshaller.Listener getListener() {
        return new Unmarshaller.Listener() {
            @Override
            public void afterUnmarshal(Object target, Object parent) {
                if (!stack.isEmpty()) {
                    ElementContext context = stack.peek();
                    context.setJaxbObject(target);
                    context.setParent(parent);
                }
            }
        };
    }
}
