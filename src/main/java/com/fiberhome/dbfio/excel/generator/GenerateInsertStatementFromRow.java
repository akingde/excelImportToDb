package com.fiberhome.dbfio.excel.generator;

import com.fiberhome.dbfio.excel.template.Mapping;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GenerateInsertStatementFromRow implements SqlGenerator{
    @Override
    public String generate(List<Mapping> mappings) {
        return "";
    }

    @Override
    public String generate(Mapping mapping) {
        return null;
    }
}
