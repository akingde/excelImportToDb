package com.lee.dbfio.excel;

import com.lee.dbfio.excel.template.Mapping;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.List;

public interface FileImporter {
	public void importToDb(File file, int sheetIndex, List<Mapping> mappings, String table, JdbcTemplate jdbcTemplate);
}
