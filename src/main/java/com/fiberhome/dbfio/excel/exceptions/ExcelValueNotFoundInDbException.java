package com.fiberhome.dbfio.excel.exceptions;

public class ExcelValueNotFoundInDbException extends Exception{
    public ExcelValueNotFoundInDbException(){
        super("Mapping not found,please check your config xml");
    }
    public ExcelValueNotFoundInDbException(String excelVal,String table,String tableField){
        super(excelVal + "is not found in table " + "["+table+"."+tableField+"]");
    }
}
