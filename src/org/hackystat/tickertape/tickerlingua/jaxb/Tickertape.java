//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.25 at 01:04:57 PM GMT-10:00 
//


package org.hackystat.tickertape.tickerlingua.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}Description"/>
 *         &lt;element ref="{}HackystatProjectRef" maxOccurs="unbounded"/>
 *         &lt;element ref="{}HackystatUserRef" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}TwitterAccountRef" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}FacebookAccountRef" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}NabaztagRef" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Ticker"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{}id use="required""/>
 *       &lt;attribute ref="{}intervalhours use="required""/>
 *       &lt;attribute ref="{}enabled use="required""/>
 *       &lt;attribute ref="{}starttime"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "hackystatProjectRef",
    "hackystatUserRef",
    "twitterAccountRef",
    "facebookAccountRef",
    "nabaztagRef",
    "ticker"
})
@XmlRootElement(name = "Tickertape")
public class Tickertape {

    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "HackystatProjectRef", required = true)
    protected List<HackystatProjectRef> hackystatProjectRef;
    @XmlElement(name = "HackystatUserRef")
    protected List<HackystatUserRef> hackystatUserRef;
    @XmlElement(name = "TwitterAccountRef")
    protected List<TwitterAccountRef> twitterAccountRef;
    @XmlElement(name = "FacebookAccountRef")
    protected List<FacebookAccountRef> facebookAccountRef;
    @XmlElement(name = "NabaztagRef")
    protected List<NabaztagRef> nabaztagRef;
    @XmlElement(name = "Ticker", required = true)
    protected Ticker ticker;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute(required = true)
    protected String intervalhours;
    @XmlAttribute(required = true)
    protected String enabled;
    @XmlAttribute
    protected String starttime;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the hackystatProjectRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hackystatProjectRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHackystatProjectRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HackystatProjectRef }
     * 
     * 
     */
    public List<HackystatProjectRef> getHackystatProjectRef() {
        if (hackystatProjectRef == null) {
            hackystatProjectRef = new ArrayList<HackystatProjectRef>();
        }
        return this.hackystatProjectRef;
    }

    /**
     * Gets the value of the hackystatUserRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hackystatUserRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHackystatUserRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HackystatUserRef }
     * 
     * 
     */
    public List<HackystatUserRef> getHackystatUserRef() {
        if (hackystatUserRef == null) {
            hackystatUserRef = new ArrayList<HackystatUserRef>();
        }
        return this.hackystatUserRef;
    }

    /**
     * Gets the value of the twitterAccountRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the twitterAccountRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTwitterAccountRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TwitterAccountRef }
     * 
     * 
     */
    public List<TwitterAccountRef> getTwitterAccountRef() {
        if (twitterAccountRef == null) {
            twitterAccountRef = new ArrayList<TwitterAccountRef>();
        }
        return this.twitterAccountRef;
    }

    /**
     * Gets the value of the facebookAccountRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facebookAccountRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacebookAccountRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FacebookAccountRef }
     * 
     * 
     */
    public List<FacebookAccountRef> getFacebookAccountRef() {
        if (facebookAccountRef == null) {
            facebookAccountRef = new ArrayList<FacebookAccountRef>();
        }
        return this.facebookAccountRef;
    }

    /**
     * Gets the value of the nabaztagRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nabaztagRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNabaztagRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NabaztagRef }
     * 
     * 
     */
    public List<NabaztagRef> getNabaztagRef() {
        if (nabaztagRef == null) {
            nabaztagRef = new ArrayList<NabaztagRef>();
        }
        return this.nabaztagRef;
    }

    /**
     * Gets the value of the ticker property.
     * 
     * @return
     *     possible object is
     *     {@link Ticker }
     *     
     */
    public Ticker getTicker() {
        return ticker;
    }

    /**
     * Sets the value of the ticker property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ticker }
     *     
     */
    public void setTicker(Ticker value) {
        this.ticker = value;
    }

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
     * Gets the value of the intervalhours property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntervalhours() {
        return intervalhours;
    }

    /**
     * Sets the value of the intervalhours property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntervalhours(String value) {
        this.intervalhours = value;
    }

    /**
     * Gets the value of the enabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnabled(String value) {
        this.enabled = value;
    }

    /**
     * Gets the value of the starttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStarttime() {
        return starttime;
    }

    /**
     * Sets the value of the starttime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStarttime(String value) {
        this.starttime = value;
    }

}
