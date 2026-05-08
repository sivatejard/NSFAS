package com.nsfas.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read and write Excel files (.xlsx) for data-driven testing.
 */
public class ExcelUtils {

    private static final Logger log = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {}

    /** Returns all rows from a sheet as a list of maps (header -> value). */
    public static List<Map<String, String>> readSheetAsMapList(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(j), getCellValue(cell));
                }
                data.add(rowData);
            }
            log.info("Read {} rows from sheet '{}' in {}", data.size(), sheetName, filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel: " + filePath, e);
        }
        return data;
    }

    /** Returns all rows as a 2D Object array — suitable for TestNG @DataProvider. */
    public static Object[][] readSheetAs2DArray(String filePath, String sheetName) {
        List<Map<String, String>> rows = readSheetAsMapList(filePath, sheetName);
        Object[][] result = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            result[i][0] = rows.get(i);
        }
        return result;
    }

    /** Returns a specific cell value by row/col index (0-based). */
    public static String getCellData(String filePath, String sheetName, int row, int col) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row r = sheet.getRow(row);
            if (r == null) return "";
            Cell cell = r.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            return getCellValue(cell);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel cell", e);
        }
    }

    /** Writes a value to a specific cell and saves the file. */
    public static void setCellData(String filePath, String sheetName, int row, int col, String value) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row r = sheet.getRow(row);
            if (r == null) r = sheet.createRow(row);
            Cell cell = r.getCell(col);
            if (cell == null) cell = r.createCell(col);
            cell.setCellValue(value);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            log.info("Written '{}' to [{},{}] in sheet '{}'", value, row, col, sheetName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel cell", e);
        }
    }

    /** Returns number of rows with data (excluding header). */
    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            return sheet == null ? 0 : sheet.getLastRowNum();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get row count", e);
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toString()
                    : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default:      return "";
        }
    }
}
