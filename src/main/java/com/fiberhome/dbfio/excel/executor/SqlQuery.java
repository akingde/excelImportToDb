package com.fiberhome.dbfio.excel.executor;

import com.fiberhome.dbfio.excel.template.Mapping;
import org.springframework.jdbc.core.JdbcTemplate;

public interface SqlQuery {
    public String query(String sql, Mapping mapping, JdbcTemplate jdbcTemplate);
}
