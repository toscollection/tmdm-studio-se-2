package com.amalto.workbench.detailtabs.sections.handlers;

import java.util.Map;

import com.amalto.workbench.detailtabs.sections.model.LanguageInfoCollection;
import com.amalto.workbench.utils.XSDAnnotationsStructure;

class DescriptionCommitHandler extends LanguageInfoCommitHandler {

    public DescriptionCommitHandler(LanguageInfoCollection submittedLangInfos) {
        super(submittedLangInfos);
    }

    @Override
    protected Map<String, String> getOriginalLang2Info() {
        return getXSDAnnotationStruct().getDescriptions();
    }

    @Override
    protected void addLangInfo(XSDAnnotationsStructure xsdAnnoStruct, String langCode, String value) {
        xsdAnnoStruct.setDescription(langCode, value);
    }

    @Override
    protected void removeLangInfo(XSDAnnotationsStructure xsdAnnoStruct, String langcode) {
        xsdAnnoStruct.removeDescription(langcode);
    }

    @Override
    protected void updateLangInfo(XSDAnnotationsStructure xsdAnnoStruct, String langCode, String value) {
        xsdAnnoStruct.setDescription(langCode, value);
    }
}
