// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation （1.1.2_01，编译版 R40）
// Generated source version: 1.1.2

package com.amalto.workbench.webservices;


public class WSTransformerProcessStep {
    protected java.lang.String pluginJNDI;
    protected java.lang.String description;
    protected java.lang.String parameters;
    protected com.amalto.workbench.webservices.WSTransformerVariablesMapping[] inputMappings;
    protected com.amalto.workbench.webservices.WSTransformerVariablesMapping[] outputMappings;
    protected java.lang.Boolean disabled;
    
    public WSTransformerProcessStep() {
    }
    
    public WSTransformerProcessStep(java.lang.String pluginJNDI, java.lang.String description, java.lang.String parameters, com.amalto.workbench.webservices.WSTransformerVariablesMapping[] inputMappings, com.amalto.workbench.webservices.WSTransformerVariablesMapping[] outputMappings, java.lang.Boolean disabled) {
        this.pluginJNDI = pluginJNDI;
        this.description = description;
        this.parameters = parameters;
        this.inputMappings = inputMappings;
        this.outputMappings = outputMappings;
        this.disabled = disabled;
    }
    
    public java.lang.String getPluginJNDI() {
        return pluginJNDI;
    }
    
    public void setPluginJNDI(java.lang.String pluginJNDI) {
        this.pluginJNDI = pluginJNDI;
    }
    
    public java.lang.String getDescription() {
        return description;
    }
    
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    public java.lang.String getParameters() {
        return parameters;
    }
    
    public void setParameters(java.lang.String parameters) {
        this.parameters = parameters;
    }
    
    public com.amalto.workbench.webservices.WSTransformerVariablesMapping[] getInputMappings() {
        return inputMappings;
    }
    
    public void setInputMappings(com.amalto.workbench.webservices.WSTransformerVariablesMapping[] inputMappings) {
        this.inputMappings = inputMappings;
    }
    
    public com.amalto.workbench.webservices.WSTransformerVariablesMapping[] getOutputMappings() {
        return outputMappings;
    }
    
    public void setOutputMappings(com.amalto.workbench.webservices.WSTransformerVariablesMapping[] outputMappings) {
        this.outputMappings = outputMappings;
    }
    
    public java.lang.Boolean getDisabled() {
        return disabled;
    }
    
    public void setDisabled(java.lang.Boolean disabled) {
        this.disabled = disabled;
    }
}
