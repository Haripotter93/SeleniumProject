package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

    public FileInputStream fi;
    public FileOutputStream fo;
    public XSSFWorkbook workbook;
    public XSSFSheet sheet;
    public XSSFRow row;
    public XSSFCell cell;
    public CellStyle style;

    String path;

    public ExcelUtility(String path) {
        this.path = path;
    }

    // Get number of rows
    public int getRowCount(String sheetName) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);

        int rowcount = sheet.getLastRowNum();

        workbook.close();
        fi.close();

        return rowcount;
    }

    // Get number of cells in a row
    public int getCellCount(String sheetName, int rownum) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);

        int cellcount = row.getLastCellNum();

        workbook.close();
        fi.close();

        return cellcount;
    }

    // Get cell data using row number and column number
    public String getCellData(String sheetName, int rownum, int colnum)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);

        if (row == null) {
            workbook.close();
            fi.close();
            return "";
        }

        cell = row.getCell(colnum);

        if (cell == null) {
            workbook.close();
            fi.close();
            return "";
        }

        String data = cell.toString();

        workbook.close();
        fi.close();

        return data;
    }

    // Get cell data using column name
    public String getCellData(String sheetName, String columnName, int rownum)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);

        int colnum = -1;

        row = sheet.getRow(0);

        for (int i = 0; i < row.getLastCellNum(); i++) {

            if (row.getCell(i).toString().equals(columnName)) {
                colnum = i;
                break;
            }
        }

        if (colnum == -1) {
            workbook.close();
            fi.close();
            return "";
        }

        row = sheet.getRow(rownum);

        if (row == null) {
            workbook.close();
            fi.close();
            return "";
        }

        cell = row.getCell(colnum);

        String data = cell == null ? "" : cell.toString();

        workbook.close();
        fi.close();

        return data;
    }

    // Set cell data
    public void setCellData(String sheetName, int rownum, int colnum, String data)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        row = sheet.getRow(rownum);

        if (row == null) {
            row = sheet.createRow(rownum);
        }

        cell = row.getCell(colnum);

        if (cell == null) {
            cell = row.createCell(colnum);
        }

        cell.setCellValue(data);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }

    // Set cell data using column name
    public void setCellData(String sheetName, String columnName, int rownum, String data)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            workbook.close();
            fi.close();
            return;
        }

        row = sheet.getRow(0);

        int colnum = -1;

        for (int i = 0; i < row.getLastCellNum(); i++) {

            if (row.getCell(i).toString().equals(columnName)) {
                colnum = i;
                break;
            }
        }

        if (colnum == -1) {
            workbook.close();
            fi.close();
            return;
        }

        row = sheet.getRow(rownum);

        if (row == null) {
            row = sheet.createRow(rownum);
        }

        cell = row.getCell(colnum);

        if (cell == null) {
            cell = row.createCell(colnum);
        }

        cell.setCellValue(data);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }

    // Apply green color to a cell
    public void fillGreenColor(String sheetName, int rownum, int colnum)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }

    // Apply red color to a cell
    public void fillRedColor(String sheetName, int rownum, int colnum)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }
}