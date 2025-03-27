package com.example.excelanalysis.service;

import java.util.List;

import com.example.excelanalysis.dto.ExcelDataDTO;
import com.example.excelanalysis.model.CalculatedData;
import com.example.excelanalysis.model.SourceData;
import com.example.excelanalysis.repository.CalculatedDataRepository;
import com.example.excelanalysis.repository.SourceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DataProcessingService {

    @Autowired
    private SourceDataRepository sourceDataRepository;

    @Autowired
    private CalculatedDataRepository calculatedDataRepository;

    @Transactional
    public void processExcelData(List<ExcelDataDTO> excelDataList) {
        for (ExcelDataDTO excelData : excelDataList) {
            // 根据业务类型和日期从source_data表查询数据
            List<SourceData> sourceDataList = sourceDataRepository.findByBusinessTypeAndTime(
                    excelData.getBusinessType(), 
                    excelData.getTime()
            );

            if (!sourceDataList.isEmpty()) {
                // 对于同一天同一业务类型的多条记录，我们需要合并处理
                SourceData mergedSourceData = mergeSourceData(sourceDataList);
                
                // 将合并后的源数据和Excel数据整合到calculated_data表中
                CalculatedData calculatedData = calculateData(mergedSourceData, excelData);
                calculatedDataRepository.save(calculatedData);
            } else {
                // 如果没有找到对应的源数据，记录日志
                System.out.println("未找到对应的源数据: 业务类型=" + excelData.getBusinessType() + ", 时间=" + excelData.getTime());
            }
        }
    }

    /**
     * 合并同一天同一业务类型的多条源数据记录
     * @param sourceDataList 源数据列表
     * @return 合并后的源数据
     */
    private SourceData mergeSourceData(List<SourceData> sourceDataList) {
        SourceData mergedData = new SourceData();
        
        // 使用第一条记录的基本信息
        SourceData firstData = sourceDataList.get(0);
        mergedData.setBusinessType(firstData.getBusinessType());
        mergedData.setNetworkType(firstData.getNetworkType());
        mergedData.setTime(firstData.getTime());
        
        // 累加所有数值字段
        long receivedDataCount = 0L;
        long receivedFileCount = 0L;
        long phoneNullCount = 0L;
        long domainNullCount = 0L;
        long destIpNullCount = 0L;
        long destPortNullCount = 0L;
        long sourceIpNullCount = 0L;
        long sourcePortNullCount = 0L;
        
        for (SourceData data : sourceDataList) {
            receivedDataCount += data.getReceivedDataCount() != null ? data.getReceivedDataCount() : 0;
            receivedFileCount += data.getReceivedFileCount() != null ? data.getReceivedFileCount() : 0;
            phoneNullCount += data.getPhoneNullCount() != null ? data.getPhoneNullCount() : 0;
            domainNullCount += data.getDomainNullCount() != null ? data.getDomainNullCount() : 0;
            destIpNullCount += data.getDestIpNullCount() != null ? data.getDestIpNullCount() : 0;
            destPortNullCount += data.getDestPortNullCount() != null ? data.getDestPortNullCount() : 0;
            sourceIpNullCount += data.getSourceIpNullCount() != null ? data.getSourceIpNullCount() : 0;
            sourcePortNullCount += data.getSourcePortNullCount() != null ? data.getSourcePortNullCount() : 0;
        }
        
        // 设置合并后的值
        mergedData.setReceivedDataCount(receivedDataCount);
        mergedData.setReceivedFileCount(receivedFileCount);
        mergedData.setPhoneNullCount(phoneNullCount);
        mergedData.setDomainNullCount(domainNullCount);
        mergedData.setDestIpNullCount(destIpNullCount);
        mergedData.setDestPortNullCount(destPortNullCount);
        mergedData.setSourceIpNullCount(sourceIpNullCount);
        mergedData.setSourcePortNullCount(sourcePortNullCount);
        
        return mergedData;
    }

    private CalculatedData calculateData(SourceData source, ExcelDataDTO excel) {
        CalculatedData calculated = new CalculatedData();
        
        // 从source_data表获取的数据
        calculated.setBusinessType(source.getBusinessType());  // 业务类型
        calculated.setNetworkType(source.getNetworkType());   // 网络类型
        calculated.setTime(source.getTime());  // 时间
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
    
    private Double calculateRate(Long nullCount, Long totalCount) {
        if (totalCount == null || totalCount == 0) {
            return 0.0;
        }
        return nullCount == null ? 0.0 : (double) nullCount / totalCount;
    }
}