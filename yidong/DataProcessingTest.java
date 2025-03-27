import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟数据处理测试
 */
public class DataProcessingTest {
    public static void main(String[] args) {
        // 模拟源数据
        SourceData sourceData = new SourceData();
        sourceData.setId(1L);
        sourceData.setBusinessType("24IOT_4GLog_2B");
        sourceData.setTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        sourceData.setReceivedDataCount(800);  // 接收数据量
        sourceData.setReceivedFileCount(8);    // 接收文件量
        sourceData.setPhoneNullCount(40);      // 电话号码空值数量
        sourceData.setDomainNullCount(30);     // 域名空值数量
        sourceData.setDestIpNullCount(20);     // 目的IP空值数量
        sourceData.setDestPortNullCount(15);   // 目的端口空值数量
        sourceData.setSourceIpNullCount(25);   // 源IP空值数量
        sourceData.setSourcePortNullCount(10); // 源端口空值数量

        // 模拟Excel数据
        ExcelDataDTO excelData = new ExcelDataDTO();
        excelData.setBusinessType("24IOT_4GLog_2B");
        excelData.setTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        excelData.setTotalDataCount(1000);     // 入库数据量
        excelData.setTotalFileCount(10);       // 入库文件量

        // 模拟计算逻辑
        CalculatedData calculatedData = calculateData(sourceData, excelData);
        
        // 打印结果
        System.out.println("=== 源数据 ===");
        System.out.println("业务类型: " + sourceData.getBusinessType());
        System.out.println("时间: " + formatDateTime(sourceData.getTime()));
        System.out.println("接收数据量: " + sourceData.getReceivedDataCount());
        System.out.println("接收文件量: " + sourceData.getReceivedFileCount());
        System.out.println("电话号码空值数量: " + sourceData.getPhoneNullCount());
        
        System.out.println("\n=== Excel数据 ===");
        System.out.println("业务类型: " + excelData.getBusinessType());
        System.out.println("时间: " + formatDateTime(excelData.getTime()));
        System.out.println("入库数据量: " + excelData.getTotalDataCount());
        System.out.println("入库文件量: " + excelData.getTotalFileCount());
        
        System.out.println("\n=== 计算结果 ===");
        System.out.println("业务类型: " + calculatedData.getBusinessType());
        System.out.println("时间: " + formatDateTime(calculatedData.getTime()));
        System.out.println("接收数据量: " + calculatedData.getReceivedCount());
        System.out.println("入库数据量: " + calculatedData.getTotalDataCount());
        System.out.println("接收文件量: " + calculatedData.getReceivedFileCount());
        System.out.println("入库文件量: " + calculatedData.getTotalFileCount());
        System.out.println("电话号码回填率: " + formatPercent(calculatedData.getPhoneNullRate()));
        System.out.println("域名回填率: " + formatPercent(calculatedData.getDomainNullRate()));
        System.out.println("目的IP回填率: " + formatPercent(calculatedData.getDestIpNullRate()));
        System.out.println("目的端口回填率: " + formatPercent(calculatedData.getDestPortNullRate()));
        System.out.println("源IP回填率: " + formatPercent(calculatedData.getSourceIpNullRate()));
        System.out.println("源端口回填率: " + formatPercent(calculatedData.getSourcePortNullRate()));
    }
    
    private static CalculatedData calculateData(SourceData source, ExcelDataDTO excel) {
        CalculatedData calculated = new CalculatedData();
        
        // 从source_data表获取的数据
        calculated.setBusinessType(source.getBusinessType());  // 业务类型
        calculated.setTime(source.getTime());                 // 时间
        calculated.setReceivedCount(source.getReceivedDataCount());     // 接收数据量
        calculated.setReceivedFileCount(source.getReceivedFileCount()); // 接收文件量
        
        // 从Excel获取的数据（入库量）
        calculated.setTotalDataCount(excel.getTotalDataCount());     // 入库数据量
        calculated.setTotalFileCount(excel.getTotalFileCount());     // 入库文件量
        
        // 计算回填率 = 空值数量/接收条数
        if (source.getReceivedDataCount() != null && source.getReceivedDataCount() > 0) {
            calculated.setPhoneNullRate(calculateRate(source.getPhoneNullCount(), source.getReceivedDataCount()));
            calculated.setDomainNullRate(calculateRate(source.getDomainNullCount(), source.getReceivedDataCount()));
            calculated.setDestIpNullRate(calculateRate(source.getDestIpNullCount(), source.getReceivedDataCount()));
            calculated.setDestPortNullRate(calculateRate(source.getDestPortNullCount(), source.getReceivedDataCount()));
            calculated.setSourceIpNullRate(calculateRate(source.getSourceIpNullCount(), source.getReceivedDataCount()));
            calculated.setSourcePortNullRate(calculateRate(source.getSourcePortNullCount(), source.getReceivedDataCount()));
        }
        
        return calculated;
    }
    
    private static Double calculateRate(Integer nullCount, Integer totalCount) {
        if (totalCount == null || totalCount == 0) {
            return 0.0;
        }
        return nullCount == null ? 0.0 : (double) nullCount / totalCount;
    }
    
    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    private static String formatPercent(Double value) {
        return String.format("%.2f%%", value * 100);
    }
    
    // 模拟实体类
    static class SourceData {
        private Long id;
        private String businessType;
        private LocalDateTime time;
        private Integer receivedDataCount;
        private Integer receivedFileCount;
        private Integer phoneNullCount;
        private Integer domainNullCount;
        private Integer destIpNullCount;
        private Integer destPortNullCount;
        private Integer sourceIpNullCount;
        private Integer sourcePortNullCount;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getBusinessType() { return businessType; }
        public void setBusinessType(String businessType) { this.businessType = businessType; }
        
        public LocalDateTime getTime() { return time; }
        public void setTime(LocalDateTime time) { this.time = time; }
        
        public Integer getReceivedDataCount() { return receivedDataCount; }
        public void setReceivedDataCount(Integer receivedDataCount) { this.receivedDataCount = receivedDataCount; }
        
        public Integer getReceivedFileCount() { return receivedFileCount; }
        public void setReceivedFileCount(Integer receivedFileCount) { this.receivedFileCount = receivedFileCount; }
        
        public Integer getPhoneNullCount() { return phoneNullCount; }
        public void setPhoneNullCount(Integer phoneNullCount) { this.phoneNullCount = phoneNullCount; }
        
        public Integer getDomainNullCount() { return domainNullCount; }
        public void setDomainNullCount(Integer domainNullCount) { this.domainNullCount = domainNullCount; }
        
        public Integer getDestIpNullCount() { return destIpNullCount; }
        public void setDestIpNullCount(Integer destIpNullCount) { this.destIpNullCount = destIpNullCount; }
        
        public Integer getDestPortNullCount() { return destPortNullCount; }
        public void setDestPortNullCount(Integer destPortNullCount) { this.destPortNullCount = destPortNullCount; }
        
        public Integer getSourceIpNullCount() { return sourceIpNullCount; }
        public void setSourceIpNullCount(Integer sourceIpNullCount) { this.sourceIpNullCount = sourceIpNullCount; }
        
        public Integer getSourcePortNullCount() { return sourcePortNullCount; }
        public void setSourcePortNullCount(Integer sourcePortNullCount) { this.sourcePortNullCount = sourcePortNullCount; }
    }
    
    static class ExcelDataDTO {
        private String businessType;
        private LocalDateTime time;
        private Integer totalDataCount;
        private Integer totalFileCount;
        
        // Getters and setters
        public String getBusinessType() { return businessType; }
        public void setBusinessType(String businessType) { this.businessType = businessType; }
        
        public LocalDateTime getTime() { return time; }
        public void setTime(LocalDateTime time) { this.time = time; }
        
        public Integer getTotalDataCount() { return totalDataCount; }
        public void setTotalDataCount(Integer totalDataCount) { this.totalDataCount = totalDataCount; }
        
        public Integer getTotalFileCount() { return totalFileCount; }
        public void setTotalFileCount(Integer totalFileCount) { this.totalFileCount = totalFileCount; }
    }
    
    static class CalculatedData {
        private Long id;
        private String businessType;
        private LocalDateTime time;
        private Integer totalDataCount;
        private Integer receivedCount;
        private Integer totalFileCount;
        private Integer receivedFileCount;
        private Double phoneNullRate;
        private Double domainNullRate;
        private Double destIpNullRate;
        private Double destPortNullRate;
        private Double sourceIpNullRate;
        private Double sourcePortNullRate;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getBusinessType() { return businessType; }
        public void setBusinessType(String businessType) { this.businessType = businessType; }
        
        public LocalDateTime getTime() { return time; }
        public void setTime(LocalDateTime time) { this.time = time; }
        
        public Integer getTotalDataCount() { return totalDataCount; }
        public void setTotalDataCount(Integer totalDataCount) { this.totalDataCount = totalDataCount; }
        
        public Integer getReceivedCount() { return receivedCount; }
        public void setReceivedCount(Integer receivedCount) { this.receivedCount = receivedCount; }
        
        public Integer getTotalFileCount() { return totalFileCount; }
        public void setTotalFileCount(Integer totalFileCount) { this.totalFileCount = totalFileCount; }
        
        public Integer getReceivedFileCount() { return receivedFileCount; }
        public void setReceivedFileCount(Integer receivedFileCount) { this.receivedFileCount = receivedFileCount; }
        
        public Double getPhoneNullRate() { return phoneNullRate; }
        public void setPhoneNullRate(Double phoneNullRate) { this.phoneNullRate = phoneNullRate; }
        
        public Double getDomainNullRate() { return domainNullRate; }
        public void setDomainNullRate(Double domainNullRate) { this.domainNullRate = domainNullRate; }
        
        public Double getDestIpNullRate() { return destIpNullRate; }
        public void setDestIpNullRate(Double destIpNullRate) { this.destIpNullRate = destIpNullRate; }
        
        public Double getDestPortNullRate() { return destPortNullRate; }
        public void setDestPortNullRate(Double destPortNullRate) { this.destPortNullRate = destPortNullRate; }
        
        public Double getSourceIpNullRate() { return sourceIpNullRate; }
        public void setSourceIpNullRate(Double sourceIpNullRate) { this.sourceIpNullRate = sourceIpNullRate; }
        
        public Double getSourcePortNullRate() { return sourcePortNullRate; }
        public void setSourcePortNullRate(Double sourcePortNullRate) { this.sourcePortNullRate = sourcePortNullRate; }
    }
}