//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.02 at 08:59:01 AM HST 
//


package org.hackystat.tickertape.tickerlingua.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Mail" minOccurs="0"/>
 *         &lt;element ref="{}LoggingLevel" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mail",
    "loggingLevel"
})
@XmlRootElement(name = "Globals")
public class Globals {

    @XmlElement(name = "Mail")
    protected Mail mail;
    @XmlElement(name = "LoggingLevel")
    protected LoggingLevel loggingLevel;

    /**
     * Gets the value of the mail property.
     * 
     * @return
     *     possible object is
     *     {@link Mail }
     *     
     */
    public Mail getMail() {
        return mail;
    }

    /**
     * Sets the value of the mail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mail }
     *     
     */
    public void setMail(Mail value) {
        this.mail = value;
    }

    /**
     * Gets the value of the loggingLevel property.
     * 
     * @return
     *     possible object is
     *     {@link LoggingLevel }
     *     
     */
    public LoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * Sets the value of the loggingLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoggingLevel }
     *     
     */
    public void setLoggingLevel(LoggingLevel value) {
        this.loggingLevel = value;
    }

}
