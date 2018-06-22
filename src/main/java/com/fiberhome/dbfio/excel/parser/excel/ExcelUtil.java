package com.fiberhome.dbfio.excel.parser.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static Workbook getWorkbook(File f) {
        FileInputStream is = null; // 文件流
        try {
            checkExcelVaild(f);
            is = new FileInputStream(f);
            return WorkbookFactory.create(is); // 这种方式 Excel2003/2007/2010都是可以处理的
        } catch (FileNotFoundException e) {
            logger.error("file {} is not exist",f.getAbsolutePath());
            return null;
        } catch (Exception e) {
            logger.error("file {} is not an excel file",f.getAbsolutePath());
            return null;
        }
    }

    /**
     * 判断文件是否是excel
     *
     * @throws Exception
     */
    private static void checkExcelVaild(File file) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
            throw new Exception("文件不是Excel");
        }
    }

    public static String getValueFromCell(Cell cell) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        if (cell == null)
            return "";
        int cellType = cell.getCellType();
        String cellValue = "";
        switch (cellType) {
            case Cell.CELL_TYPE_STRING: // 文本
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC: // 数字、日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = fmt.format(cell.getDateCellValue());
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK: // 空白
                cellValue = cell.getStringCellValue();
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }
}
