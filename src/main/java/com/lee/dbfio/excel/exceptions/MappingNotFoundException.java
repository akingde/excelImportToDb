package com.lee.dbfio.excel.exceptions;

public class MappingNotFoundException extends Exception{
    public MappingNotFoundException(){
        super("Mapping not found,please check your config xml");
    }
}
