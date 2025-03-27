package com.example.excelanalysis.util;

import com.example.excelanalysis.dto.ExcelDataDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    
    /**
     * 读取Excel文件内容
     * @param inputStream Excel文件输入流
     * @return 解析后的数据列表
     * @throws IOException 如果文件读取出错
     */
    public static List<ExcelDataDTO> readExcelFile(InputStream inputStream) throws IOException {
        logger.info("开始读取Excel文件");
        List<ExcelDataDTO> resultList = new ArrayList<>();
        
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);  // 获取第一个工作表
            
            // 跳过标题行
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();  // 跳过标题行
            }
            
            // 遍历数据行
            int rowNum = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    ExcelDataDTO excelData = parseExcelRow(row);
                    if (excelData != null) {
                        resultList.add(excelData);
                    }
                } catch (Exception e) {
                    logger.error("解析第 {} 行数据时出错: {}", rowNum + 1, e.getMessage());
                }
                rowNum++;
            }
        }
        
        logger.info("成功解析 {} 条Excel记录", resultList.size());
        return resultList;
    }
    
    /**
     * 读取CSV文件内容
     * @param inputStream CSV文件输入流
     * @return 解析后的数据列表
     * @throws IOException 如果文件读取出错
     */
    public static List<ExcelDataDTO> readCsvFile(InputStream inputStream) throws IOException {
        logger.info("开始读取CSV文件");
        List<ExcelDataDTO> resultList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line = reader.readLine();  // 跳过标题行
            
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                try {
                    // 处理CSV中的引号和逗号
                    String[] values = parseCsvLine(line);
                    if (values.length >= 4) {  // 确保有足够的列
                        ExcelDataDTO excelData = new ExcelDataDTO();
                        
                        // 解析业务类型
                        String businessType = values[0].trim();
                        if (businessType.isEmpty()) {
                            logger.warn("第 {} 行业务类型为空", lineNum + 1);
                            continue;
                        }
                        excelData.setBusinessType(businessType);
                        
                        // 解析时间
                        String dateStr = values[1].trim();
                        try {
                            LocalDate date;
                            if (dateStr.contains("/")) {
                                // 处理可能的 MM/dd/yyyy 格式
                                DateTimeFormatter slashFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                                date = LocalDate.parse(dateStr, slashFormatter);
                            } else {
                                // 默认 yyyy-MM-dd 格式
                                DateTimeFormatter dashFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                date = LocalDate.parse(dateStr, dashFormatter);
                            }
                            excelData.setTime(date);
                        } catch (DateTimeParseException e) {
                            logger.warn("第 {} 行日期格式错误 (应为 yyyy-MM-dd 或 MM/dd/yyyy): {}", lineNum + 1, dateStr);
                            continue;
                        }
                        
                        // 解析入库数据量
                        String dataCountStr = values[2].trim();
                        try {
                            long dataCount = parseNumber(dataCountStr);
                            if (dataCount < 0) {
                                logger.warn("第 {} 行入库数据量不能为负数: {}", lineNum + 1, dataCountStr);
                                continue;
                            }
                            excelData.setTotalDataCount(dataCount);
                        } catch (NumberFormatException e) {
                            logger.warn("第 {} 行入库数据量格式错误: {}", lineNum + 1, dataCountStr);
                            continue;
                        }
                        
                        // 解析入库文件量
                        String fileCountStr = values[3].trim();
                        try {
                            long fileCount = parseNumber(fileCountStr);
                            if (fileCount < 0) {
                                logger.warn("第 {} 行入库文件量不能为负数: {}", lineNum + 1, fileCountStr);
                                continue;
                            }
                            excelData.setTotalFileCount(fileCount);
                        } catch (NumberFormatException e) {
                            logger.warn("第 {} 行入库文件量格式错误: {}", lineNum + 1, fileCountStr);
                            continue;
                        }
                        
                        resultList.add(excelData);
                    } else {
                        logger.warn("第 {} 行数据列数不足: 需要4列，实际{}列", lineNum + 1, values.length);
                    }
                } catch (Exception e) {
                    logger.error("解析第 {} 行CSV数据时出错: {}", lineNum + 1, e.getMessage());
                }
                lineNum++;
            }
        }
        
        logger.info("成功解析 {} 条CSV记录", resultList.size());
        return resultList;
    }
    
    /**
     * 解析Excel行数据
     * @param row Excel行
     * @return 解析后的数据对象
     */
    private static ExcelDataDTO parseExcelRow(Row row) {
        // 检查行是否为空
        if (isRowEmpty(row)) {
            return null;
        }
        
        ExcelDataDTO excelData = new ExcelDataDTO();
        
        // 解析业务类型
        Cell businessTypeCell = row.getCell(0);
        if (businessTypeCell != null) {
            excelData.setBusinessType(getCellValueAsString(businessTypeCell));
        } else {
            logger.warn("第 {} 行业务类型为空", row.getRowNum() + 1);
            return null;
        }
        
        // 解析时间
        Cell timeCell = row.getCell(1);
        if (timeCell != null) {
            try {
                String dateStr = getCellValueAsString(timeCell);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateStr, formatter);
                excelData.setTime(date);
            } catch (DateTimeParseException e) {
                logger.warn("第 {} 行日期格式错误: {}", row.getRowNum() + 1, getCellValueAsString(timeCell));
                return null;
            }
        } else {
            logger.warn("第 {} 行时间为空", row.getRowNum() + 1);
            return null;
        }
        
        // 解析入库数据量
        Cell totalDataCountCell = row.getCell(2);
        if (totalDataCountCell != null) {
            try {
                excelData.setTotalDataCount((long) totalDataCountCell.getNumericCellValue());
            } catch (Exception e) {
                logger.warn("第 {} 行入库数据量格式错误", row.getRowNum() + 1);
                return null;
            }
        } else {
            logger.warn("第 {} 行入库数据量为空", row.getRowNum() + 1);
            return null;
        }
        
        // 解析入库文件量
        Cell totalFileCountCell = row.getCell(3);
        if (totalFileCountCell != null) {
            try {
                excelData.setTotalFileCount((long) totalFileCountCell.getNumericCellValue());
            } catch (Exception e) {
                logger.warn("第 {} 行入库文件量格式错误", row.getRowNum() + 1);
                return null;
            }
        } else {
            logger.warn("第 {} 行入库文件量为空", row.getRowNum() + 1);
            return null;
        }
        
        return excelData;
    }
    
    /**
     * 检查行是否为空
     * @param row Excel行
     * @return 是否为空
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        
        for (int i = 0; i < 4; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取单元格的字符串值
     * @param cell Excel单元格
     * @return 单元格的字符串值
     */
    /**
     * 解析CSV行，处理引号和逗号
     * @param line CSV行
     * @return 解析后的字段数组
     */
    private static String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // 处理引号
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // 处理转义的引号 ("") -> (")
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // 找到字段分隔符
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        
        // 添加最后一个字段
        tokens.add(sb.toString());
        
        return tokens.toArray(new String[0]);
    }
    
    /**
     * 解析数字，支持科学计数法和千分位分隔符
     * @param str 要解析的字符串
     * @return 解析后的长整型数值
     */
    private static long parseNumber(String str) {
        // 移除千分位分隔符
        str = str.replace(",", "");
        
        // 尝试解析科学计数法或普通数字
        if (str.contains("E") || str.contains("e")) {
            return Math.round(Double.parseDouble(str));
        } else {
            return Long.parseLong(str);
        }
    }

    /**
     * 获取单元格的字符串值
     * @param cell Excel单元格
     * @return 单元格的字符串值
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    try {
                        // 尝试获取日期值
                        return DateUtil.getJavaDate(cell.getNumericCellValue()).toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate().toString();
                    } catch (Exception e) {
                        logger.warn("无法解析日期单元格值，尝试作为数值处理: {}", e.getMessage());
                        return String.valueOf(Math.round(cell.getNumericCellValue()));
                    }
                } else {
                    double value = cell.getNumericCellValue();
                    // 如果是整数，去掉小数部分
                    if (value == Math.floor(value)) {
                        return String.valueOf(Math.round(value));
                    }
                    return String.valueOf(value);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            default:
                return "";
        }
    }
}