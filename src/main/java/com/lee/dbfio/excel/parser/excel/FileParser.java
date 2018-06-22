package com.lee.dbfio.excel.parser.excel;

import com.lee.dbfio.excel.exceptions.ExcelValueNotFoundInDbException;
import com.lee.dbfio.excel.template.Mapping;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface FileParser {
	public Map<String,Mapping> parse(Row row, List<Mapping> tableMapping, JdbcTemplate jdbcTemplate) throws ExcelValueNotFoundInDbException;
}
