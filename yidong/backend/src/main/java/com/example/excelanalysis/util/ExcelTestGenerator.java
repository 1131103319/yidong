package com.example.excelanalysis.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class ExcelTestGenerator {
    public static void main(String[] args) {
        try {
            String outputPath = args.length > 0 ? args[0] : "test.xlsx";
            
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("测试数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"业务类型", "时间", "总数据条数", "总文件数"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 添加数据行1
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("24IOT_4GLog_2B");
            row1.createCell(1).setCellValue("2024-01-01");
            row1.createCell(2).setCellValue(1000L);
            row1.createCell(3).setCellValue(10L);
            
            // 添加数据行2
            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("4G_4GLog_2B");
            row2.createCell(1).setCellValue("2024-01-01");
            row2.createCell(2).setCellValue(2000L);
            row2.createCell(3).setCellValue(20L);
            
            // 写入文件
            FileOutputStream out = new FileOutputStream(outputPath);
            workbook.write(out);
            out.close();
            workbook.close();
            
            System.out.println(outputPath + " 文件已成功创建");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}