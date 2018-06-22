package com.lee.dbfio.excel;


import com.lee.dbfio.excel.executor.StatementSqlExecutor;
import com.lee.dbfio.excel.parser.excel.ExcelUtil;
import com.lee.dbfio.excel.parser.excel.SqlExcelRowParser;
import com.lee.dbfio.excel.template.Mapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileImporterBySql implements FileImporter {
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    private SqlExcelRowParser sqlExcelRowParser;
    @Autowired
    private StatementSqlExecutor statementSqlExecutor;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void importToDb(File file, int sheetIndex, List<Mapping> tableMapping, String table, JdbcTemplate jdbcTemplate) {
        Workbook workbook = null;
        try {
            workbook = ExcelUtil.getWorkbook(file);
            if(workbook==null) {
                return;
            }
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            Integer begin = Integer.parseInt(tableMapping.get(0).getDataRowBegin());

            List<Map<String,Mapping>> rowFieldMappings = new ArrayList<>();
            int isTableHead = 0;
            int total = sheet.getLastRowNum();
            for (Row row : sheet) {
                if (isTableHead<begin) {//表头不处理
                    isTableHead++;
                    continue;
                }
                if(row.getRowNum()>total)
                    break;

                rowFieldMappings.add(sqlExcelRowParser.parse(row, tableMapping,jdbcTemplate));
            }
            statementSqlExecutor.executeBatch(rowFieldMappings,jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
