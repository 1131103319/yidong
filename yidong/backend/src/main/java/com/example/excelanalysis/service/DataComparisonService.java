package com.example.excelanalysis.service;

import com.example.excelanalysis.dto.DataStatisticsResponse;
import com.example.excelanalysis.dto.ExcelDataDTO;
import com.example.excelanalysis.model.CalculatedData;
import com.example.excelanalysis.model.SourceData;
import com.example.excelanalysis.repository.CalculatedDataRepository;
import com.example.excelanalysis.repository.SourceDataRepository;
import com.example.excelanalysis.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataComparisonService {
    private static final Logger logger = LoggerFactory.getLogger(DataComparisonService.class);

    @Autowired
    private SourceDataRepository sourceDataRepository;

    @Autowired
    private CalculatedDataRepository calculatedDataRepository;

    @Autowired
    private DataProcessingService dataProcessingService;

    /**
     * 处理上传的Excel文件
     * @param file 上传的文件
     * @param type 处理类型
     * @return 处理结果
     * @throws IOException 如果文件处理出错
     */
    @Transactional
    public Map<String, Object> processExcelUpload(MultipartFile file, String type) throws IOException {
        logger.info("开始处理上传的Excel文件，类型: {}", type);
        Map<String, Object> result = new HashMap<>();

        try (InputStream is = file.getInputStream()) {
            // 判断文件类型
            String fileName = file.getOriginalFilename();
            List<ExcelDataDTO> excelDataList;

            if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                // 处理CSV文件
                excelDataList = ExcelUtil.readCsvFile(is);
            } else {
                // 处理Excel文件
                excelDataList = ExcelUtil.readExcelFile(is);
            }

            // 数据验证
            if (excelDataList.isEmpty()) {
                logger.warn("上传的文件中没有有效数据");
                result.put("success", false);
                result.put("message", "上传的文件中没有有效数据");
                return result;
            }

            // 处理数据
            dataProcessingService.processExcelData(excelDataList);

            logger.info("成功处理 {} 条记录", excelDataList.size());
            result.put("success", true);
            result.put("message", "成功处理 " + excelDataList.size() + " 条记录");
            result.put("recordCount", excelDataList.size());
            return result;
        } catch (Exception e) {
            logger.error("处理Excel文件时发生错误", e);
            result.put("success", false);
            result.put("message", "处理文件失败: " + e.getMessage());
            throw new IOException("处理文件失败", e);
        }
    }

    /**
     * 生成Excel模板
     * @return 模板文件的字节数组
     * @throws IOException 如果生成模板出错
     */
    public byte[] generateExcelTemplate() throws IOException {
        logger.info("开始生成Excel模板");
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("数据导入模板");
            
            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"业务类型", "时间(YYYY-MM-DD)", "入库数据量", "入库文件量"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 256 * 20);  // 设置列宽
            }
            
            // 添加示例数据
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("24IOT_4GLog_2B");
            exampleRow.createCell(1).setCellValue("2024-01-01");
            exampleRow.createCell(2).setCellValue(12000);
            exampleRow.createCell(3).setCellValue(120);
            
            // 将工作簿写入字节数组
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            logger.info("Excel模板生成成功");
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error("生成Excel模板失败", e);
            throw new IOException("生成Excel模板失败", e);
        }
    }

    /**
     * 获取统计数据
     * @param queryDate 查询日期
     * @param networkType 网络类型
     * @return 统计数据响应
     */
    public DataStatisticsResponse getDataStatistics(LocalDate queryDate, String networkType) {
        logger.info("获取统计数据 - 日期: {}, 网络类型: {}", queryDate, networkType);
        
        // 从数据库获取计算结果数据
        List<CalculatedData> calculatedDataList = calculatedDataRepository.findByTimeAndNetworkType(
                queryDate, networkType);
        
        if (calculatedDataList.isEmpty()) {
            logger.warn("未找到符合条件的数据 - 日期: {}, 网络类型: {}", queryDate, networkType);
            return new DataStatisticsResponse();
        }
        
        // 构建响应对象
        DataStatisticsResponse response = new DataStatisticsResponse();
        
        // 设置基本信息
        response.setDate(queryDate.toString());
        response.setNetworkType(networkType);
        
        // 直接将原始数据转换为前端需要的格式
        List<Map<String, Object>> formattedData = new ArrayList<>();
        for (CalculatedData data : calculatedDataList) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", data.getTime().toString());
            item.put("networkType", data.getNetworkType());
            item.put("business", data.getBusinessType());
            item.put("totalDataCount", data.getTotalDataCount());
            item.put("receivedCount", data.getReceivedCount());
            item.put("totalFileCount", data.getTotalFileCount());
            item.put("receivedFileCount", data.getReceivedFileCount());
            item.put("phoneNullRate", data.getPhoneNullRate());
            item.put("domainNullRate", data.getDomainNullRate());
            item.put("destIpNullRate", data.getDestIpNullRate());
            item.put("destPortNullRate", data.getDestPortNullRate());
            item.put("sourceIpNullRate", data.getSourceIpNullRate());
            item.put("sourcePortNullRate", data.getSourcePortNullRate());
            formattedData.add(item);
        }
        
        // 创建响应Map
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", formattedData);
        responseMap.put("total", calculatedDataList.size());
        
        // 将responseMap设置到response对象中
        response.setResponseMap(responseMap);
        
        logger.info("统计数据获取成功，找到 {} 条记录", calculatedDataList.size());
        return response;
    }

    /**
     * 导出Excel数据
     * @param date 日期字符串
     * @param networkType 网络类型
     * @return 包含Excel数据的资源
     */
    public Resource exportExcel(String date, String networkType) {
        logger.info("开始导出Excel数据 - 日期: {}, 网络类型: {}", date, networkType);
        
        try {
            // 解析日期
            LocalDate queryDate = null;
            if (date != null && !date.trim().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                queryDate = LocalDate.parse(date, formatter);
            }
            
            // 查询数据
            List<CalculatedData> dataList;
            if (queryDate != null && networkType != null && !networkType.trim().isEmpty()) {
                dataList = calculatedDataRepository.findByTimeAndNetworkType(queryDate, networkType);
            } else if (queryDate != null) {
                dataList = calculatedDataRepository.findByTime(queryDate);
            } else if (networkType != null && !networkType.trim().isEmpty()) {
                dataList = calculatedDataRepository.findByNetworkType(networkType);
            } else {
                dataList = calculatedDataRepository.findAll();
            }
            
            // 生成Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("数据比对结果");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "业务类型", "网络类型", "日期", 
                "入库数据量", "接收数据量", "入库文件量", "接收文件量",
                "手机号空值率", "域名空值率", "目标IP空值率", 
                "目标端口空值率", "源IP空值率", "源端口空值率"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                sheet.setColumnWidth(i, 256 * 15);  // 设置列宽
            }
            
            // 填充数据
            int rowNum = 1;
            for (CalculatedData data : dataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(data.getBusinessType());
                row.createCell(1).setCellValue(data.getNetworkType());
                row.createCell(2).setCellValue(data.getTime().toString());
                row.createCell(3).setCellValue(data.getTotalDataCount());
                row.createCell(4).setCellValue(data.getReceivedCount());
                row.createCell(5).setCellValue(data.getTotalFileCount());
                row.createCell(6).setCellValue(data.getReceivedFileCount());
                row.createCell(7).setCellValue(formatRate(data.getPhoneNullRate()));
                row.createCell(8).setCellValue(formatRate(data.getDomainNullRate()));
                row.createCell(9).setCellValue(formatRate(data.getDestIpNullRate()));
                row.createCell(10).setCellValue(formatRate(data.getDestPortNullRate()));
                row.createCell(11).setCellValue(formatRate(data.getSourceIpNullRate()));
                row.createCell(12).setCellValue(formatRate(data.getSourcePortNullRate()));
            }
            
            // 写入字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            
            logger.info("Excel数据导出成功，共 {} 条记录", dataList.size());
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            logger.error("导出Excel数据失败", e);
            throw new RuntimeException("导出Excel数据失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 格式化百分比
     * @param rate 比率
     * @return 格式化后的字符串
     */
    private String formatRate(Double rate) {
        if (rate == null) {
            return "0.00%";
        }
        return String.format("%.2f%%", rate * 100);
    }
}