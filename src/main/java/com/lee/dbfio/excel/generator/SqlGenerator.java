package com.lee.dbfio.excel.generator;


import com.lee.dbfio.excel.template.Mapping;

import java.util.List;

public interface SqlGenerator {
    public String generate(List<Mapping> mappings);
    public String generate(Mapping mapping);
}
