/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.service.calltransformer.ejb.local;

/**
 * Local interface for CallTransformer.
 * @xdoclet-generated at 17-06-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface CallTransformerLocal
   extends javax.ejb.EJBLocalObject
{

   public java.lang.String getJNDIName(  ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getDescription( java.lang.String twoLetterLanguageCode ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getDocumentation( java.lang.String twoLettersLanguageCode ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getStatus(  ) throws com.amalto.core.util.XtentisException;

   public void start(  ) throws com.amalto.core.util.XtentisException;

   public void stop(  ) throws com.amalto.core.util.XtentisException;

   public java.io.Serializable receiveFromOutbound( java.util.HashMap map ) throws com.amalto.core.util.XtentisException;

   public java.lang.String receiveFromInbound( com.amalto.core.ejb.ItemPOJOPK itemPK,java.lang.String routingOrderID,java.lang.String parameters ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getConfiguration( java.lang.String optionalParameters ) throws com.amalto.core.util.XtentisException;

   public void putConfiguration( java.lang.String configuration ) throws com.amalto.core.util.XtentisException;

}
