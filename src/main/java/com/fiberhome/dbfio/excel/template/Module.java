package com.fiberhome.dbfio.excel.template;

import com.fiberhome.dbfio.excel.template.annotation.XPath;

import java.util.List;

@XPath("fio/excelModules/excelModule")
public class Module {
    @XPath("./@id")
    private String id;

    @XPath("./@dataRowBegin")
    private String dataRowBegin = 1+"";

    private List<InsertTable>tables;

    public String getDataRowBegin() {
        return dataRowBegin;
    }

    public void setDataRowBegin(String dataRowBegin) {
        this.dataRowBegin = dataRowBegin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<InsertTable> getTables() {
        return tables;
    }

    public void setTables(List<InsertTable> tables) {
        this.tables = tables;
    }
}
