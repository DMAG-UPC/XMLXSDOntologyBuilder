//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.03.14 a las 12:52:08 PM CET 
//


package org.example.ontology;

import jakarta.annotation.Generated;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DatatypeType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DatatypeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;any/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatatypeType", propOrder = {
    "any"
})
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
public class DatatypeType {

    @XmlAnyElement(lax = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    protected Object any;

    /**
     * Obtiene el valor de la propiedad any.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public Object getAny() {
        return any;
    }

    /**
     * Define el valor de la propiedad any.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-03-14T12:52:08+01:00")
    public void setAny(Object value) {
        this.any = value;
    }

}
