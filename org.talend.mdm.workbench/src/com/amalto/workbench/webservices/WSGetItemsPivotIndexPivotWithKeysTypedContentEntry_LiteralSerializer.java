// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation （1.1.2_01，编译版 R40）
// Generated source version: 1.1.2

package com.amalto.workbench.webservices;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.xsd.XSDConstants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class WSGetItemsPivotIndexPivotWithKeysTypedContentEntry_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final QName ns1_key_QNAME = new QName("", "key");
    private static final QName ns3_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;
    private CombinedSerializer ns3_myns3_string__java_lang_String_String_Serializer;
    private static final QName ns1_value_QNAME = new QName("", "value");
    private static final QName ns2_WSStringArray_TYPE_QNAME = new QName("urn-com-amalto-xtentis-webservice", "WSStringArray");
    private CombinedSerializer ns2_myWSStringArray_LiteralSerializer;
    
    public WSGetItemsPivotIndexPivotWithKeysTypedContentEntry_LiteralSerializer(QName type, String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public WSGetItemsPivotIndexPivotWithKeysTypedContentEntry_LiteralSerializer(QName type, String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns3_myns3_string__java_lang_String_String_Serializer = (CombinedSerializer)registry.getSerializer("", java.lang.String.class, ns3_string_TYPE_QNAME);
        ns2_myWSStringArray_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", com.amalto.workbench.webservices.WSStringArray.class, ns2_WSStringArray_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.amalto.workbench.webservices.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry instance = new com.amalto.workbench.webservices.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry();
        Object member=null;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_key_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_key_QNAME, reader, context);
                instance.setKey((java.lang.String)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_key_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_value_QNAME)) {
                member = ns2_myWSStringArray_LiteralSerializer.deserialize(ns1_value_QNAME, reader, context);
                instance.setValue((com.amalto.workbench.webservices.WSStringArray)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_value_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.workbench.webservices.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry instance = (com.amalto.workbench.webservices.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.workbench.webservices.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry instance = (com.amalto.workbench.webservices.WSGetItemsPivotIndexPivotWithKeysTypedContentEntry)obj;
        
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getKey(), ns1_key_QNAME, null, writer, context);
        ns2_myWSStringArray_LiteralSerializer.serialize(instance.getValue(), ns1_value_QNAME, null, writer, context);
    }
}
