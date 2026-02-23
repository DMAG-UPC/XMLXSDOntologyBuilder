//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.03.14 a las 12:52:08 PM CET 
//


package org.example.ontology;

import jakarta.annotation.Generated;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Clase Java para restType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="restType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/1999/02/22-rdf-syntax-ns#}DescriptionType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="resource" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restType", propOrder = {
    "description"
})
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
public class RestType {

    @XmlElementRef(name = "Description", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", type = JAXBElement.class, required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    protected Serializable description;
    @XmlAttribute(name = "resource", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    protected String resource;
    @XmlAnyAttribute
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();


    /**
     * Obtiene el valor de la propiedad description.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public Serializable getDescription() {
        return description;
    }

    /**
     * Define el valor de la propiedad description.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public void setDescription(Serializable value) {
        this.description = value;
    }

    /**
     * Obtiene el valor de la propiedad resource.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public String getResource() {
        return resource;
    }

    /**
     * Define el valor de la propiedad resource.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public void setResource(String value) {
        this.resource = value;
    }
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
