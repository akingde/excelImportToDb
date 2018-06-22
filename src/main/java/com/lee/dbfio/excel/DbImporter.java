package com.lee.dbfio.excel;

import com.lee.dbfio.excel.exceptions.MappingNotFoundException;
import com.lee.dbfio.excel.parser.xml.Xml2Object;
import com.lee.dbfio.excel.template.InsertTable;
import com.lee.dbfio.excel.template.Mapping;
import com.lee.dbfio.excel.template.Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbImporter {
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static Map<String, List<InsertTable>> tableMappings = new HashMap<>();

    private FileImporter fileImporter;

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FileImporter getFileImporter() {
        return fileImporter;
    }

    public void setFileImporter(FileImporter fileImporter) {
        this.fileImporter = fileImporter;
    }

    /**
     * 将文件第index sheet页的数据导入到数据库表名为tableName的表中
     *
     * @param file       excel文件
     * @param configKey  数据库中对应表的表名
     * @param sheetIndex excel文件数据所在sheet的index
     */
    public void importToDb(File file, int sheetIndex, String configKey) throws MappingNotFoundException {
        logger.debug("import file path:{}",file.getAbsolutePath());
        List<Module> modules = getObjectFromXml();
        if (tableMappings.size() == 0) {
            for (Module module : modules) {
                logger.debug("module id {} data row begin index {}",module.getId(),module.getDataRowBegin()==null?1+"":module.getDataRowBegin());
                for (InsertTable insertTable : module.getTables()) {
                    logger.debug("table Name {}",insertTable.getInsertTable());
                    insertTable.setDataRowBegin(module.getDataRowBegin()==null?1+"":module.getDataRowBegin());
                }
                tableMappings.put(module.getId(), module.getTables());
            }
        }

        //将insertTable节点的table属性值放入到其下所有mapping的insertTable属性中备用
        for (List<InsertTable> insertTables : tableMappings.values()) {
            for (InsertTable insertTable : insertTables) {
                for (Mapping mapping : insertTable.getMappings()) {
                    logger.debug(mapping.toString());
                    mapping.setInsertTable(insertTable.getInsertTable());
                    mapping.setDataRowBegin(insertTable.getDataRowBegin());
                }
            }
        }

        //找到导入文件对应的module配置,一个配置中可能会向多个表中添加数据
        List<InsertTable> tableModules = tableMappings.get(configKey);
        if (tableModules == null || tableModules.size() == 0)
            throw new MappingNotFoundException();

        //处理配置中的每一个表的配置
        for (InsertTable tableModule : tableModules) {
            logger.debug("module: {},table:{} begin to process",configKey,tableModule.getInsertTable());
            fileImporter.importToDb(file, sheetIndex, tableModule.getMappings(), configKey, jdbcTemplate);
        }

    }

    private List<Module> getObjectFromXml() {
        String classPath = this.getClass().getResource("/").getPath() + File.separator;
        Xml2Object xml2Object = new Xml2Object(classPath + "excelModule.xml");

        //将整个xml解析成多个module
        return xml2Object.getObject(Module.class);
    }
}
