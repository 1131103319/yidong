package com.example.excelanalysis.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 生成测试Excel文件，包含入库量数据
 */
public class TestExcelGenerator {
    
    public static void main(String[] args) {
        String outputPath = "test-data.xlsx";
        if (args.length > 0) {
            outputPath = args[0];
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建工作表
            Sheet sheet = workbook.createSheet("入库数据");
            
            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"业务类型", "时间", "总数据条数", "总文件数"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }
            
            // 添加数据
            addDataRow(sheet, 1, "24IOT_4GLog_2B", "2024-01-01", 1000, 10);
            addDataRow(sheet, 2, "24IOT_4GLog_2B", "2024-01-02", 1500, 15);
            addDataRow(sheet, 3, "24IOT_4GLog_2B", "2024-01-03", 1200, 12);
            
            addDataRow(sheet, 4, "4G_4GLog_2B", "2024-01-01", 2500, 25);
            addDataRow(sheet, 5, "4G_4GLog_2B", "2024-01-02", 3000, 30);
            addDataRow(sheet, 6, "4G_4GLog_2B", "2024-01-03", 2200, 22);
            
            addDataRow(sheet, 7, "NB_4GLog_2B", "2024-01-01", 1800, 18);
            addDataRow(sheet, 8, "NB_4GLog_2B", "2024-01-02", 2100, 21);
            addDataRow(sheet, 9, "NB_4GLog_2B", "2024-01-03", 1900, 19);
            
            addDataRow(sheet, 10, "2G_4GLog_2B", "2024-01-01", 600, 6);
            addDataRow(sheet, 11, "2G_4GLog_2B", "2024-01-02", 700, 7);
            addDataRow(sheet, 12, "2G_4GLog_2B", "2024-01-03", 650, 6);
            
            addDataRow(sheet, 13, "5G_4GLog_2B", "2024-01-01", 3500, 35);
            addDataRow(sheet, 14, "5G_4GLog_2B", "2024-01-02", 4000, 40);
            addDataRow(sheet, 15, "5G_4GLog_2B", "2024-01-03", 3800, 38);
            
            // 添加特殊场景数据
            addDataRow(sheet, 16, "24IOT_4GLog_2B", "2024-01-04", 1200, 12);
            addDataRow(sheet, 17, "24IOT_4GLog_2B", "2024-01-05", 1300, 13);
            addDataRow(sheet, 18, "4G_4GLog_2B", "2024-01-04", 1200, 12);
            
            // 写入文件
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
                System.out.println("Excel文件已创建: " + outputPath);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void addDataRow(Sheet sheet, int rowNum, String businessType, String date, 
                                  int totalDataCount, int totalFileCount) {
        Row row = sheet.createRow(rowNum);
        
        // 业务类型
        Cell cell0 = row.createCell(0);
        cell0.setCellValue(businessType);
        
        // 日期
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(date);
        
        // 总数据条数
        Cell cell2 = row.createCell(2);
        cell2.setCellValue(totalDataCount);
        
        // 总文件数
        Cell cell3 = row.createCell(3);
        cell3.setCellValue(totalFileCount);
    }
}