package com.fiberhome.dbfio.excel.parser.xml;

import com.fiberhome.dbfio.excel.template.Mapping;

import java.util.List;
import java.util.Map;


public interface XmlParse{
    public Map<String, List<Mapping>> parseTemplateFile(String path);
    public Map<String, List<Mapping>> parseTemplateFile();
}
