
package com.amalto.workbench.webservices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WSStoredProcedurePKArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WSStoredProcedurePKArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsStoredProcedurePK" type="{urn-com-amalto-xtentis-webservice}WSStoredProcedurePK" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSStoredProcedurePKArray", propOrder = {
    "wsStoredProcedurePK"
})
public class WSStoredProcedurePKArray {

    @XmlElement(required = true)
    protected List<WSStoredProcedurePK> wsStoredProcedurePK;

    /**
     * Default no-arg constructor
     * 
     */
    public WSStoredProcedurePKArray() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public WSStoredProcedurePKArray(final List<WSStoredProcedurePK> wsStoredProcedurePK) {
        this.wsStoredProcedurePK = wsStoredProcedurePK;
    }

    /**
     * Gets the value of the wsStoredProcedurePK property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wsStoredProcedurePK property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWsStoredProcedurePK().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSStoredProcedurePK }
     * 
     * 
     */
    public List<WSStoredProcedurePK> getWsStoredProcedurePK() {
        if (wsStoredProcedurePK == null) {
            wsStoredProcedurePK = new ArrayList<WSStoredProcedurePK>();
        }
        return this.wsStoredProcedurePK;
    }

}
