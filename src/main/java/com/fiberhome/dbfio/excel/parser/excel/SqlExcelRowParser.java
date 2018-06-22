package com.fiberhome.dbfio.excel.parser.excel;

import com.fiberhome.dbfio.excel.exceptions.ExcelValueNotFoundInDbException;
import com.fiberhome.dbfio.excel.executor.StatementSqlExecutor;
import com.fiberhome.dbfio.excel.generator.GenerateInsertStatementFromRow;
import com.fiberhome.dbfio.excel.generator.GenerateQueryStatementFromMapping;
import com.fiberhome.dbfio.excel.template.Mapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将excel里的每一行（除表头外）数据解析到mapping里去
 */
@Component
public class SqlExcelRowParser implements FileParser {
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    @Autowired
    private GenerateQueryStatementFromMapping generateQueryStatementFromMapping;

    @Autowired
    private GenerateInsertStatementFromRow generateInsertStatementFromRow;

    @Autowired
    private StatementSqlExecutor statementSqlExecutor;

    private static Map<String,String> excelTableMap = new HashMap<>();

    private Map<String,Mapping> parseRowInfoIntoTableMapping(Row row, List<Mapping> tableMapping, JdbcTemplate jdbcTemplate) throws ExcelValueNotFoundInDbException {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD");
        Map<String,Mapping> result = new HashMap<>();

        logger.info("begin to parse row {}",row.getRowNum());

        for (Mapping mapping : tableMapping) {
            String excelColumnIndex = mapping.getExcelColumnIndex();
            String valueType = mapping.getInsertType();

            if(valueType.equalsIgnoreCase("default")) {
                logger.info("column {} value type {} original value {}",mapping.getExcelColumnIndex(),valueType,mapping.getDefaultValue());
                String defaultValue = mapping.getDefaultValue().equalsIgnoreCase("sysdate")?sdf.format(new Date()):mapping.getDefaultValue();
                mapping.setValue(defaultValue);
            }else if (valueType.equalsIgnoreCase("ref")) {
                String valTemp = ExcelUtil.getValueFromCell(row.getCell(Integer.parseInt(excelColumnIndex)));
                logger.info("column {} value type {} original value {}",mapping.getExcelColumnIndex(),valueType,valTemp);
                mapping.setValue(valTemp);
                mapping.setExcelVal(valTemp);
                String sqlStr = generateQueryStatementFromMapping.generate(mapping);
                String refVal = statementSqlExecutor.query(sqlStr,mapping,jdbcTemplate);
                if(refVal==null||refVal.equalsIgnoreCase("")) {
                    logger.error("row num {} mapping info {}", row.getRowNum(), mapping.toString());
                    throw new ExcelValueNotFoundInDbException(valTemp,mapping.getQueryTable(),mapping.getQueryField());
                }
                mapping.setValue(refVal);
            }else if(valueType.equalsIgnoreCase("in")) {
                String valTemp = ExcelUtil.getValueFromCell(row.getCell(Integer.parseInt(excelColumnIndex)));
                logger.info("column {} value type {} original value {}",mapping.getExcelColumnIndex(),valueType,valTemp);
                mapping.setValue(valTemp);
                mapping.setExcelVal(valTemp);
            }else if(valueType.equalsIgnoreCase("map")) {
                String excelValue = ExcelUtil.getValueFromCell(row.getCell(Integer.parseInt(excelColumnIndex)));
                mapping.setExcelVal(excelValue);
                String[] mapKeys = mapping.getExcelValues().split(",");
                String[] mapVals = mapping.getTableValues().split(",");
                logger.info("column {} value type {} original value {} key->val {}->{}",mapping.getExcelColumnIndex(),valueType,excelValue,mapKeys,mapVals);
                int index = 0;
                for(String t : mapKeys){
                    if(t.equals(excelValue))
                        break;
                    else
                        index++;
                }

                mapping.setValue(mapVals[index]);
            }
            try {
                mapping = mapping.clone();
            } catch (CloneNotSupportedException e) {
                //已实现clone方法，异常不处理
            }
            result.put(mapping.getInsertField(),mapping);
            logger.info("field: {}, mapping: {}",mapping.getInsertField(),mapping.toString());
        }
        return result;
    }

    @Override
    public Map<String,Mapping> parse(Row row, List<Mapping> tableMapping, JdbcTemplate jdbcTemplate) throws ExcelValueNotFoundInDbException {
        logger.info("begin to parse row {}",row.getRowNum());
        Map<String,Mapping> rtn = parseRowInfoIntoTableMapping(row, tableMapping,jdbcTemplate);
        logger.info("parse row {} end",row.getRowNum());
        return rtn;
    }

}
