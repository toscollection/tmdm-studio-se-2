
package com.amalto.workbench.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WSDeleteDataModel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WSDeleteDataModel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsDataModelPK" type="{urn-com-amalto-xtentis-webservice}WSDataModelPK"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSDeleteDataModel", propOrder = {
    "wsDataModelPK"
})
public class WSDeleteDataModel {

    @XmlElement(required = true)
    protected WSDataModelPK wsDataModelPK;

    /**
     * Default no-arg constructor
     * 
     */
    public WSDeleteDataModel() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public WSDeleteDataModel(final WSDataModelPK wsDataModelPK) {
        this.wsDataModelPK = wsDataModelPK;
    }

    /**
     * Gets the value of the wsDataModelPK property.
     * 
     * @return
     *     possible object is
     *     {@link WSDataModelPK }
     *     
     */
    public WSDataModelPK getWsDataModelPK() {
        return wsDataModelPK;
    }

    /**
     * Sets the value of the wsDataModelPK property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSDataModelPK }
     *     
     */
    public void setWsDataModelPK(WSDataModelPK value) {
        this.wsDataModelPK = value;
    }

}
