// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation （1.1.2_01，编译版 R40）
// Generated source version: 1.1.2

package com.amalto.workbench.webservices;


public class WSVersioningItemsVersionsItems {
    protected com.amalto.workbench.webservices.WSItemPK wsItemPK;
    protected com.amalto.workbench.webservices.WSVersioningHistoryEntry[] wsVersionEntries;
    
    public WSVersioningItemsVersionsItems() {
    }
    
    public WSVersioningItemsVersionsItems(com.amalto.workbench.webservices.WSItemPK wsItemPK, com.amalto.workbench.webservices.WSVersioningHistoryEntry[] wsVersionEntries) {
        this.wsItemPK = wsItemPK;
        this.wsVersionEntries = wsVersionEntries;
    }
    
    public com.amalto.workbench.webservices.WSItemPK getWsItemPK() {
        return wsItemPK;
    }
    
    public void setWsItemPK(com.amalto.workbench.webservices.WSItemPK wsItemPK) {
        this.wsItemPK = wsItemPK;
    }
    
    public com.amalto.workbench.webservices.WSVersioningHistoryEntry[] getWsVersionEntries() {
        return wsVersionEntries;
    }
    
    public void setWsVersionEntries(com.amalto.workbench.webservices.WSVersioningHistoryEntry[] wsVersionEntries) {
        this.wsVersionEntries = wsVersionEntries;
    }
}
