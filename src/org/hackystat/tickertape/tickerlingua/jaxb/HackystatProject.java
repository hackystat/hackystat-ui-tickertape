//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.25 at 01:04:57 PM GMT-10:00 
//


package org.hackystat.tickertape.tickerlingua.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute ref="{}id use="required""/>
 *       &lt;attribute ref="{}name use="required""/>
 *       &lt;attribute ref="{}shortname use="required""/>
 *       &lt;attribute ref="{}hackystatservice-refid use="required""/>
 *       &lt;attribute ref="{}projectowner-refid use="required""/>
 *       &lt;attribute ref="{}authuser-refid use="required""/>
 *       &lt;attribute ref="{}mailinglist use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "HackystatProject")
public class HackystatProject {

    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String shortname;
    @XmlAttribute(name = "hackystatservice-refid", required = true)
    protected String hackystatserviceRefid;
    @XmlAttribute(name = "projectowner-refid", required = true)
    protected String projectownerRefid;
    @XmlAttribute(name = "authuser-refid", required = true)
    protected String authuserRefid;
    @XmlAttribute(required = true)
    protected String mailinglist;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the shortname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * Sets the value of the shortname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortname(String value) {
        this.shortname = value;
    }

    /**
     * Gets the value of the hackystatserviceRefid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHackystatserviceRefid() {
        return hackystatserviceRefid;
    }

    /**
     * Sets the value of the hackystatserviceRefid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHackystatserviceRefid(String value) {
        this.hackystatserviceRefid = value;
    }

    /**
     * Gets the value of the projectownerRefid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectownerRefid() {
        return projectownerRefid;
    }

    /**
     * Sets the value of the projectownerRefid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectownerRefid(String value) {
        this.projectownerRefid = value;
    }

    /**
     * Gets the value of the authuserRefid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthuserRefid() {
        return authuserRefid;
    }

    /**
     * Sets the value of the authuserRefid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthuserRefid(String value) {
        this.authuserRefid = value;
    }

    /**
     * Gets the value of the mailinglist property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailinglist() {
        return mailinglist;
    }

    /**
     * Sets the value of the mailinglist property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailinglist(String value) {
        this.mailinglist = value;
    }

}