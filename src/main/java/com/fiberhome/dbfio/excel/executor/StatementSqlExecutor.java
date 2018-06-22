package com.fiberhome.dbfio.excel.executor;

import com.fiberhome.dbfio.excel.template.Mapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StatementSqlExecutor implements SqlExecutor, SqlQuery {
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private String getInsertSqlTemplate (){
        return new String("insert into ${tableName} (${columns}) values (${values})");
    }

    @Override
    public void executeBatch(List<Map<String, Mapping>> fieldValueMappings, JdbcTemplate jdbcTemplate) {
        //每个记录用的模板都是一样的，只用从第一条记录中获取一次就可以了
        Map<String, Mapping> rowInfo = fieldValueMappings.get(0);
        String sqlTemplate = getInsertSqlTemplate();
        boolean isTableSet = false;
        StringBuilder fieldStringBuilder = new StringBuilder();
        StringBuilder paramStringBuilder = new StringBuilder();
        for (Mapping mapping : rowInfo.values()) {
            if (!isTableSet) {
                sqlTemplate = sqlTemplate.replaceAll("\\$\\{tableName\\}", mapping.getInsertTable());
                fieldStringBuilder.append(mapping.getInsertField());
                paramStringBuilder.append("?");
                isTableSet = true;
                continue;
            }
            fieldStringBuilder.append("," + mapping.getInsertField());
            paramStringBuilder.append("," + "?");
        }
        sqlTemplate = sqlTemplate.replaceAll("\\$\\{columns\\}", fieldStringBuilder.toString());
        sqlTemplate = sqlTemplate.replaceAll("\\$\\{values\\}", paramStringBuilder.toString());

        logger.info("execute sql template is : "+sqlTemplate);

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD");

        jdbcTemplate.batchUpdate(sqlTemplate, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int index = 0;
                for (Mapping mapping : fieldValueMappings.get(i).values()) {
                    index++;
                    String jdbcType = mapping.getFieldJdbcType() == null ? "varchar" : mapping.getFieldJdbcType().toLowerCase();
                    logger.info("mapping info that preparedStatement to be set is:{}",mapping.toString());
                    try {
                        switch (jdbcType) {
                            case "integer":
                                int intValue = Integer.parseInt(mapping.getValue());
                                ps.setInt(index, intValue);
                                break;
                            case "double":
                                double doubleValue = Double.parseDouble(mapping.getValue());
                                ps.setDouble(index, doubleValue);
                                break;
                            case "date":
                                Date dateValue = new Date(sdf.parse(mapping.getValue()).getTime());
                                ps.setDate(index,dateValue);
                                break;
                            case "varchar":
                                ps.setString(index,mapping.getValue());
                                break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("sql info: {}",ps.toString());
            }

            @Override
            public int getBatchSize() {
                return fieldValueMappings.size();
            }
        });
    }

    @Override
    public void execute(Map<String, Mapping> fieldMapping,JdbcTemplate jdbcTemplate) {
    }

    @Override
    public String query(String sql, Mapping mapping,JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(sql, new QueryPreparedStatementSetter(mapping), new QueryResultSetExtractor(mapping));
    }

    /**
     * 查询结果返回策略
     */
    private class QueryResultSetExtractor implements ResultSetExtractor<String> {
        private Mapping mapping;

        public QueryResultSetExtractor() {
        }

        public QueryResultSetExtractor(Mapping mapping) {
            this.mapping = mapping;
        }

        @Override
        public String extractData(ResultSet rs) throws SQLException, DataAccessException {
            String rtnVal = "";
            while (rs.next()) {
                rtnVal = rs.getInt(mapping.getQueryResult()) + "";
            }
            return rtnVal;
        }
    }

    /**
     * 查询参数设置策略
     */
    private class QueryPreparedStatementSetter implements PreparedStatementSetter {
        private List<Mapping> mappings = new ArrayList<>();

        public QueryPreparedStatementSetter(Mapping mapping) {
            this.mappings.add(mapping);
        }

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
            for (int i = 0; i < mappings.size(); i++) {
                Mapping mapping = mappings.get(i);
                String valStr = mapping.getValue();
                ps.setString(i + 1, valStr);
            }
        }
    }
}
