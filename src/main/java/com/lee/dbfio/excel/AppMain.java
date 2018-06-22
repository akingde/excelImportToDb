package com.lee.dbfio.excel;

import com.lee.dbfio.excel.exceptions.MappingNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class AppMain {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        DbImporter dbImporter = (DbImporter) ctx.getBean("dbImporter");
        try {
            //dbImporter.importToDb(new File("C:\\Users\\liwanbing\\Desktop\\项目资料\\物料采购订单.xlsx"), 0, "cpn_purchase_order");
            dbImporter.importToDb(new File("C:\\Users\\liwanbing\\Desktop\\物料采购订单详情 (恢复的)(已自动还原).xlsx"), 0, "cpn_order_detail");
        } catch (MappingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
