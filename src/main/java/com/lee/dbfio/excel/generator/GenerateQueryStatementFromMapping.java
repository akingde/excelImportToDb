package com.lee.dbfio.excel.generator;

import com.lee.dbfio.excel.template.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerateQueryStatementFromMapping implements SqlGenerator {
    @Override
    public String generate(List<Mapping> mappings) {
        return null;
    }

    @Override
    public String generate(Mapping mapping) {
        String sqlTemplate = "select ${result} from ${dataTable} where ${conditionField} = ${condition}";

        String result = mapping.getQueryResult();
        String dataTable = mapping.getQueryTable();
        String conditionField = mapping.getQueryField();

        sqlTemplate = sqlTemplate.replaceAll("\\$\\{result\\}",result);
        sqlTemplate = sqlTemplate.replaceAll("\\$\\{dataTable\\}",dataTable);
        sqlTemplate = sqlTemplate.replaceAll("\\$\\{conditionField\\}",conditionField);
        sqlTemplate = sqlTemplate.replaceAll("\\$\\{condition\\}","?");

        return sqlTemplate;
    }
}
