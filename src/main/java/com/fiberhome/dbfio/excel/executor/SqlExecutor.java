package com.fiberhome.dbfio.excel.executor;

import com.fiberhome.dbfio.excel.template.Mapping;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface SqlExecutor {
	public void execute(Map<String, Mapping> fieldMapping, JdbcTemplate jdbcTemplate);
	public void executeBatch(List<Map<String, Mapping>> fieldValueMappings, JdbcTemplate jdbcTemplate);
}
