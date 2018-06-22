package com.fiberhome.dbfio.excel.generator;


import com.fiberhome.dbfio.excel.template.Mapping;

import java.util.List;

public interface SqlGenerator {
    public String generate(List<Mapping> mappings);
    public String generate(Mapping mapping);
}
