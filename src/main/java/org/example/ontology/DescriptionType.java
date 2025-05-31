//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.04.04 a las 11:13:05 AM CEST 
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
 * <p>Clase Java para DescriptionType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DescriptionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.w3.org/1999/02/22-rdf-syntax-ns#}typeType"/&gt;
 *         &lt;element name="first" type="{http://www.w3.org/1999/02/22-rdf-syntax-ns#}firstType"/&gt;
 *         &lt;element name="rest" type="{http://www.w3.org/1999/02/22-rdf-syntax-ns#}restType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DescriptionType", propOrder = {
    "type",
    "first",
    "rest"
})
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-04-04T11:13:05+02:00")
public class DescriptionType {

    @XmlElementRef(name = "type", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", type = JAXBElement.class, required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    protected Serializable type;
    @XmlElementRef(name = "first", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", type = JAXBElement.class, required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    protected Serializable first;
    @XmlElementRef(name = "rest", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", type = JAXBElement.class, required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    protected Serializable rest;
    @XmlAnyAttribute
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-04-04T11:13:05+02:00")
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Obtiene el valor de la propiedad type.
     *
     * @return
     *     possible object is
     *     {@link TypeType }
     *
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public Serializable getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     *
     * @param value
     *     allowed object is
     *     {@link TypeType }
     *
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public void setType(Serializable value) {
        this.type = value;
    }

    /**
     * Obtiene el valor de la propiedad first.
     *
     * @return
     *     possible object is
     *     {@link FirstType }
     *
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public Serializable getFirst() {
        return first;
    }

    /**
     * Define el valor de la propiedad first.
     *
     * @param value
     *     allowed object is
     *     {@link FirstType }
     *
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public void setFirst(Serializable value) {
        this.first = value;
    }

    /**
     * Obtiene el valor de la propiedad rest.
     *
     * @return
     *     possible object is
     *     {@link RestType }
     *
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public Serializable getRest() { return rest; }

    /**
     * Define el valor de la propiedad rest.
     *
     * @param value
     *     allowed object is
     *     {@link RestType }
     *
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public void setRest(Serializable value) {
        this.rest = value;
    }


    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-04-04T11:13:05+02:00")
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
