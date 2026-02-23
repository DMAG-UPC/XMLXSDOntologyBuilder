package org.example.XMLParserHelper;

class ElementContext {
    final String qName;
    final int startLine;
    Object jaxbObject;
    Object parent;

    ElementContext(String qName, int startLine) {
        this.qName = qName;
        this.startLine = startLine;
    }

    public void setJaxbObject(Object jaxbObject) {
        this.jaxbObject = jaxbObject;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
}
