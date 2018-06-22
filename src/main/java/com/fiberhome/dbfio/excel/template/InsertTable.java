package com.fiberhome.dbfio.excel.template;

import com.fiberhome.dbfio.excel.template.annotation.XPath;

import java.util.List;

@XPath("fio/excelModules/excelModule/insertTable")
public class InsertTable{
    @XPath("./@table")
    private String insertTable;

    private String dataRowBegin = 1+"";

    private List<Mapping> mappings;

    public String getDataRowBegin() {
        return dataRowBegin;
    }

    public void setDataRowBegin(String dataRowBegin) {
        this.dataRowBegin = dataRowBegin;
    }

    public String getInsertTable() {
        return insertTable;
    }

    public void setInsertTable(String insertTable) {
        this.insertTable = insertTable;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }
}
