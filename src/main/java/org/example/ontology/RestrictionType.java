//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.02.08 a las 12:29:11 PM CET 
//


package org.example.ontology;

import jakarta.annotation.Generated;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>Clase Java para RestrictionType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RestrictionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="onProperty" type="{http://www.w3.org/2002/07/owl#}onPropertyType"/&gt;
 *         &lt;element name="someValuesFrom" type="{http://www.w3.org/2002/07/owl#}someValuesFromType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictionType", propOrder = {
    "onProperty",
    "someValuesFrom"
})
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
public class RestrictionType {

    @XmlElementRef(name = "onProperty", namespace = "http://www.w3.org/2002/07/owl#", type = JAXBElement.class, required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    protected Serializable onProperty;
    @XmlElementRef(name = "someValuesFrom", namespace = "http://www.w3.org/2002/07/owl#", type = JAXBElement.class, required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    protected Serializable someValuesFrom;

    /**
     * Obtiene el valor de la propiedad onProperty.
     * 
     * @return
     *     possible object is
     *     {@link OnPropertyType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    public Serializable getOnProperty() {
        return onProperty;
    }

    /**
     * Define el valor de la propiedad onProperty.
     * 
     * @param value
     *     allowed object is
     *     {@link OnPropertyType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    public void setOnProperty(Serializable value) {
        this.onProperty = value;
    }

    /**
     * Obtiene el valor de la propiedad someValuesFrom.
     * 
     * @return
     *     possible object is
     *     {@link SomeValuesFromType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    public Serializable getSomeValuesFrom() {
        return someValuesFrom;
    }

    /**
     * Define el valor de la propiedad someValuesFrom.
     * 
     * @param value
     *     allowed object is
     *     {@link SomeValuesFromType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v3.0.0", date = "2024-02-08T12:29:11+01:00")
    public void setSomeValuesFrom(Serializable value) {
        this.someValuesFrom = value;
    }

}
