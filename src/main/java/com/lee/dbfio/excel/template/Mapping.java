package com.lee.dbfio.excel.template;

import com.lee.dbfio.excel.template.annotation.XPath;

@XPath("fio/excelModules/excelModule/mapping")
public class Mapping implements Cloneable {

    private String insertTable;

    private String dataRowBegin = 1+"";

    @XPath("./excel/@type")
    private String insertType;

    @XPath("./excel/@columnIndex")
    private String excelColumnIndex;

    @XPath("./excel/@table")
    private String queryTable;

    @XPath("./excel/@field")
    private String queryField;

    @XPath("./excel/@result")
    private String queryResult;

    @XPath("./excel/@value")
    private String defaultValue;

    @XPath("./excel/@excelValues")
    private String excelValues;

    @XPath("./excel/@tableValues")
    private String tableValues;

    @XPath("./targetColumn/@field")
    private String insertField;

    @XPath("./targetColumn/@jdbcType")
    private String fieldJdbcType;

    private String excelVal;

    private String value;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [insert Table name: "+insertTable+"]");
        sb.append(" [dataRowBegin: "+dataRowBegin+"]");
        sb.append(" [insert Type: "+insertType+"]");
        sb.append(" [excel Column Index: "+excelColumnIndex+"]");
        sb.append(" [query Table Name: " +queryTable+"]");
        sb.append(" [query Field Name: " +queryField+"]");
        sb.append(" [query Result: "+queryResult+"]");
        sb.append(" [defaultValue: "+defaultValue+"]");
        sb.append(" [excelValues: " +excelValues+"]");
        sb.append(" [tableValues: " + tableValues+"]");
        sb.append(" [insertField: " + insertField+"]");
        sb.append(" [fieldJdbcType: " + fieldJdbcType+"]");
        sb.append(" [value: " + value+"]");
        sb.append(" [excelVal: " + excelVal+"]");

        return  sb.toString();
    }

    public String getExcelVal() {
        return excelVal;
    }

    public void setExcelVal(String excelVal) {
        this.excelVal = excelVal;
    }

    public String getInsertTable() {
        return insertTable;
    }

    public void setInsertTable(String insertTable) {
        this.insertTable = insertTable;
    }

    public String getDataRowBegin() {
        return dataRowBegin;
    }

    public void setDataRowBegin(String dataRowBegin) {
        this.dataRowBegin = dataRowBegin;
    }

    public String getExcelValues() {
        return excelValues;
    }

    public void setExcelValues(String excelValues) {
        this.excelValues = excelValues;
    }

    public String getTableValues() {
        return tableValues;
    }

    public void setTableValues(String tableValues) {
        this.tableValues = tableValues;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInsertType() {
        return insertType;
    }

    public void setInsertType(String insertType) {
        this.insertType = insertType;
    }

    public String getExcelColumnIndex() {
        return excelColumnIndex;
    }

    public void setExcelColumnIndex(String excelColumnIndex) {
        this.excelColumnIndex = excelColumnIndex;
    }

    public String getQueryTable() {
        return queryTable;
    }

    public void setQueryTable(String queryTable) {
        this.queryTable = queryTable;
    }

    public String getQueryField() {
        return queryField;
    }

    public void setQueryField(String queryField) {
        this.queryField = queryField;
    }

    public String getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public String getInsertField() {
        return insertField;
    }

    public void setInsertField(String insertField) {
        this.insertField = insertField;
    }

    public String getFieldJdbcType() {
        return fieldJdbcType;
    }

    public void setFieldJdbcType(String fieldJdbcType) {
        this.fieldJdbcType = fieldJdbcType;
    }

    @Override
    public Mapping clone() throws CloneNotSupportedException {
        return (Mapping) super.clone();
    }
}
