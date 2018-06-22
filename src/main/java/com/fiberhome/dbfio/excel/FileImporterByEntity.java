package com.fiberhome.dbfio.excel;

import com.fiberhome.dbfio.excel.template.Mapping;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.List;

public class FileImporterByEntity implements FileImporter {
	@Override
	public void importToDb(File file, int sheetIndex, List<Mapping> mappings, String table, JdbcTemplate jdbcTemplate) {
		System.out.println("Not support yet");
	}
}
