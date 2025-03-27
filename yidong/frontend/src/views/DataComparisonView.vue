<template>
  <div class="data-comparison">
    <div class="header">
      <div class="operation-row">
        <div class="operation-group">
          <el-button class="custom-button" type="default" @click="downloadTemplate">下载模板</el-button>
          <el-upload
            class="upload-btn"
            action="/api/data-comparison/upload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="false">
            <el-button class="custom-button" type="primary">新增比对数据</el-button>
          </el-upload>
          <el-button class="custom-button" type="default" @click="exportData">导出比对数据</el-button>
        </div>
        <div class="operation-group">
          <el-date-picker
            v-model="selectedDate"
            class="custom-date-picker"
            type="date"
            placeholder="请选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD">
          </el-date-picker>
          <el-select 
            v-model="selectedNetworkType" 
            class="custom-select"
            placeholder="请选择网络类型">
            <el-option label="4G" value="4G"></el-option>
            <el-option label="5G" value="5G"></el-option>
          </el-select>
          <el-button class="custom-button" type="primary" @click="queryData">查询</el-button>
        </div>
      </div>
    </div>

    <div class="charts-section">
      <div class="chart-row">
        <div class="chart-box">
          <div class="chart-header">
            <div class="chart-title">采集数据完整对比图</div>
            <div class="chart-summary">
              <div class="summary-label">数据完整总差值</div>
              <div class="summary-value">{{ totalDataDifference }}</div>
            </div>
          </div>
          <div class="chart-container" ref="dataCompletionChart"></div>
        </div>
        <div class="chart-box">
          <div class="chart-header">
            <div class="chart-title">采集数据文件数对比图</div>
            <div class="chart-summary">
              <div class="summary-label">文件数总差值</div>
              <div class="summary-value">{{ totalFileDifference }}</div>
            </div>
          </div>
          <div class="chart-container" ref="fileComparisonChart"></div>
        </div>
      </div>
    </div>

    <div class="data-section">
      <div class="section-header">
        <i class="el-icon-data-line"></i> 数据统计
      </div>
      <el-table 
        :data="tableData" 
        style="width: 100%" 
        border 
        stripe>
        <el-table-column type="index" label="序号" width="70" align="center"></el-table-column>
        <el-table-column prop="business" label="业务" align="center"></el-table-column>
        <el-table-column prop="networkType" label="网络类型" align="center"></el-table-column>
        <el-table-column prop="totalDataCount" label="入库量" align="center">
          <template #default="scope">
            {{ formatNumber(scope.row.totalDataCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedCount" label="接收量" align="center">
          <template #default="scope">
            {{ formatNumber(scope.row.receivedCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalFileCount" label="文件入库量" align="center">
          <template #default="scope">
            {{ formatNumber(scope.row.totalFileCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedFileCount" label="文件接收量" align="center">
          <template #default="scope">
            {{ formatNumber(scope.row.receivedFileCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="phoneNullRate" label="用户号码回填率" align="center">
          <template #default="scope">
            {{ formatRate(scope.row.phoneNullRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="domainNullRate" label="域名回填率" align="center">
          <template #default="scope">
            {{ formatRate(scope.row.domainNullRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="destIpNullRate" label="目的IP回填率" align="center">
          <template #default="scope">
            {{ formatRate(scope.row.destIpNullRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="destPortNullRate" label="目的端口回填率" align="center">
          <template #default="scope">
            {{ formatRate(scope.row.destPortNullRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="sourceIpNullRate" label="源公网IP回填率" align="center">
          <template #default="scope">
            {{ formatRate(scope.row.sourceIpNullRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="sourcePortNullRate" label="源端口回填率" align="center">
          <template #default="scope">
            {{ formatRate(scope.row.sourcePortNullRate) }}
          </template>
        </el-table-column>
        <el-table-column prop="date" label="日期" align="center"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import axios from 'axios';
import { ElMessage, ElLoading } from 'element-plus';

export default {
  name: 'DataComparisonView',
  setup() {
    // 工具函数
    const getYesterday = () => {
      const date = new Date();
      date.setDate(date.getDate() - 1);
      return date.toISOString().split('T')[0];
    };

    const formatNumber = (value) => {
      if (value === undefined || value === null) return '0';
      return value.toLocaleString();
    };

    const formatRate = (value) => {
      if (value === undefined || value === null) return '0%';
      const percentage = Number(value) * 100;
      if (percentage < 0.01) {
        return '<0.01%';
      }
      return percentage.toFixed(2) + '%';
    };

    // 响应式数据
    const selectedDate = ref(getYesterday());
    const selectedNetworkType = ref('4G');
    const tableData = ref([]);
    const dataCompletionChart = ref(null);
    const fileComparisonChart = ref(null);
    const totalDataDifference = ref(0);
    const totalFileDifference = ref(0);
    const chartInstances = {
      dataCompletion: null,
      fileComparison: null
    };

    const initCharts = () => {
      // 使用nextTick确保DOM已经渲染完成
      setTimeout(() => {
        if (dataCompletionChart.value) {
          chartInstances.dataCompletion = echarts.init(dataCompletionChart.value);
          // 初始化时设置一个基本配置，防止未定义错误
          chartInstances.dataCompletion.setOption({
            tooltip: {},
            legend: { data: ['平台', '数据源', '差值率'] },
            xAxis: [{ type: 'category', data: [] }],
            yAxis: [
              { type: 'value', name: '数据量' },
              { type: 'value', name: '差值率' }
            ],
            series: [
              { name: '平台', type: 'bar', data: [] },
              { name: '数据源', type: 'bar', data: [] },
              { name: '差值率', type: 'line', yAxisIndex: 1, data: [] }
            ]
          });
        }
        
        if (fileComparisonChart.value) {
          chartInstances.fileComparison = echarts.init(fileComparisonChart.value);
          // 初始化时设置一个基本配置，防止未定义错误
          chartInstances.fileComparison.setOption({
            tooltip: {},
            legend: { data: ['平台', '数据源', '差值率'] },
            xAxis: [{ type: 'category', data: [] }],
            yAxis: [
              { type: 'value', name: '文件数' },
              { type: 'value', name: '差值率' }
            ],
            series: [
              { name: '平台', type: 'bar', data: [] },
              { name: '数据源', type: 'bar', data: [] },
              { name: '差值率', type: 'line', yAxisIndex: 1, data: [] }
            ]
          });
        }
      }, 100); // 给DOM一点时间渲染
    };

    const updateCharts = (data) => {
      // 确保图表实例存在，如果不存在则重新初始化
      if (!chartInstances.dataCompletion && dataCompletionChart.value) {
        chartInstances.dataCompletion = echarts.init(dataCompletionChart.value);
      }
      if (!chartInstances.fileComparison && fileComparisonChart.value) {
        chartInstances.fileComparison = echarts.init(fileComparisonChart.value);
      }

      if (!data || data.length === 0) {
        // 清空图表数据
        const emptyOption = {
          tooltip: {},
          legend: { data: ['平台', '数据源', '差值率'] },
          xAxis: [{ type: 'category', data: [] }],
          yAxis: [
            { type: 'value', name: '数据量' },
            { type: 'value', name: '差值率' }
          ],
          series: [
            { name: '平台', type: 'bar', data: [] },
            { name: '数据源', type: 'bar', data: [] },
            { name: '差值率', type: 'line', yAxisIndex: 1, data: [] }
          ]
        };
        
        if (chartInstances.dataCompletion) {
          chartInstances.dataCompletion.setOption(emptyOption);
        }
        if (chartInstances.fileComparison) {
          chartInstances.fileComparison.setOption(emptyOption);
        }
        return;
      }

      const categories = data.map(item => item.business);
      const platformData = data.map(item => item.totalDataCount);
      const sourceData = data.map(item => item.receivedCount);
      const platformFileData = data.map(item => item.totalFileCount);
      const sourceFileData = data.map(item => item.receivedFileCount);

      // 计算差值率（平台 - 数据源）/ 平台 - 确保为正值
      const diffRateData = platformData.map((val, idx) => 
        val === 0 ? 0 : Math.abs((val - sourceData[idx]) / val)
      );

      // 计算文件差值（平台 - 数据源）
      const fileDiffData = data.map(item => 
        item.totalFileCount - item.receivedFileCount
      );

      // 计算文件差值率（平台 - 数据源）/ 平台 - 确保为正值
      const fileDiffRateData = data.map(item => 
        item.totalFileCount === 0 ? 0 : 
        Math.abs((item.totalFileCount - item.receivedFileCount) / item.totalFileCount)
      );

      // 数据完整对比图配置
      const dataCompletionOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          formatter: function(params) {
            let tooltip = params[0].name + '<br/>';
            params.forEach(param => {
              if (param.seriesName === '差值率') {
                tooltip += param.seriesName + ' : ' + (param.value * 100).toFixed(2) + '%<br/>';
              } else {
                tooltip += param.seriesName + ' : ' + param.value.toLocaleString() + '<br/>';
              }
            });
            return tooltip;
          }
        },
        legend: {
          data: ['平台', '数据源', '差值率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: categories,
          axisLabel: {
            interval: 0,
            rotate: 30
          }
        }],
        yAxis: [
          {
            type: 'value',
            name: '数据量',
            axisLabel: {
              formatter: value => value.toLocaleString()
            }
          },
          {
            type: 'value',
            name: '差值率',
            axisLabel: {
              formatter: value => (value * 100).toFixed(2) + '%'
            }
          }
        ],
        series: [
          {
            name: '平台',
            type: 'bar',
            data: platformData,
            itemStyle: {
              color: '#69c0ff'
            }
          },
          {
            name: '数据源',
            type: 'bar',
            data: sourceData,
            itemStyle: {
              color: '#b7eb8f'
            }
          },
          {
            name: '差值率',
            type: 'line',
            yAxisIndex: 1,
            data: diffRateData,
            itemStyle: {
              color: '#ff7875'
            }
          }
        ]
      };

      // 文件数对比图配置
      const fileComparisonOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          formatter: function(params) {
            let tooltip = params[0].name + '<br/>';
            params.forEach(param => {
              if (param.seriesName === '差值率') {
                tooltip += param.seriesName + ' : ' + (param.value * 100).toFixed(2) + '%<br/>';
              } else {
                tooltip += param.seriesName + ' : ' + param.value.toLocaleString() + '<br/>';
              }
            });
            return tooltip;
          }
        },
        legend: {
          data: ['平台', '数据源', '差值率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: categories,
          axisLabel: {
            interval: 0,
            rotate: 30
          }
        }],
        yAxis: [
          {
            type: 'value',
            name: '文件数',
            axisLabel: {
              formatter: value => value.toLocaleString()
            }
          },
          {
            type: 'value',
            name: '差值率',
            axisLabel: {
              formatter: value => (value * 100).toFixed(2) + '%'
            }
          }
        ],
        series: [
          {
            name: '平台',
            type: 'bar',
            data: platformFileData,
            itemStyle: {
              color: '#69c0ff'
            }
          },
          {
            name: '数据源',
            type: 'bar',
            data: sourceFileData,
            itemStyle: {
              color: '#b7eb8f'
            }
          },
          {
            name: '差值率',
            type: 'line',
            yAxisIndex: 1,
            data: fileDiffRateData,
            itemStyle: {
              color: '#ff7875'
            }
          }
        ]
      };

      // 更新图表
      if (chartInstances.dataCompletion) {
        chartInstances.dataCompletion.setOption(dataCompletionOption);
      }
      
      if (chartInstances.fileComparison) {
        chartInstances.fileComparison.setOption(fileComparisonOption);
      }
    };

    const queryData = async () => {
      if (!selectedDate.value) {
        ElMessage.warning('请选择日期');
        return;
      }

      const loading = ElLoading.service({
        lock: true,
        text: '数据加载中...',
        background: 'rgba(255, 255, 255, 0.7)'
      });
      
      try {
        const response = await axios.get('/api/data-comparison/statistics', {
          params: {
            date: selectedDate.value,
            networkType: selectedNetworkType.value
          }
        });
        
          if (response.data && Array.isArray(response.data.data)) {
            tableData.value = response.data.data;
            
            if (response.data.data.length === 0) {
              // 如果没有数据，重置总差值显示
              totalDataDifference.value = '0';
              totalFileDifference.value = '0';
            } else {
              // 更新总差值（平台 - 数据源）- 使用绝对值确保为正
              const totalDataDiff = tableData.value.reduce((sum, item) => 
                sum + Math.abs(item.totalDataCount - item.receivedCount), 0);
              const totalFileDiff = tableData.value.reduce((sum, item) => 
                sum + Math.abs(item.totalFileCount - item.receivedFileCount), 0);
              
              totalDataDifference.value = formatNumber(totalDataDiff);
              totalFileDifference.value = formatNumber(totalFileDiff);
            }
            
            // 更新图表数据
            updateCharts(tableData.value);
          }
      } catch (error) {
        ElMessage.error('查询失败：' + error.message);
      } finally {
        loading.close();
      }
    };

    const downloadTemplate = async () => {
      try {
        const response = await axios.get('/api/data-comparison/template', {
          responseType: 'blob'
        });
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'template.xlsx');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (error) {
        ElMessage.error('下载模板失败');
      }
    };

    const exportData = async () => {
      try {
        const response = await axios.get('/api/data-comparison/export', {
          responseType: 'blob'
        });
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'data-comparison.xlsx');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (error) {
        ElMessage.error('导出失败');
      }
    };

    const handleUploadSuccess = () => {
      ElMessage.success('上传成功');
      queryData();
    };

    const handleUploadError = () => {
      ElMessage.error('上传失败');
    };

    const beforeUpload = (file) => {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                      file.type === 'application/vnd.ms-excel';
      if (!isExcel) {
        ElMessage.error('只能上传Excel文件！');
        return false;
      }
      return true;
    };

    const handleResize = () => {
      chartInstances.dataCompletion?.resize();
      chartInstances.fileComparison?.resize();
    };

    onMounted(() => {
      initCharts();
      queryData();
      window.addEventListener('resize', handleResize);
    });

    onUnmounted(() => {
      chartInstances.dataCompletion?.dispose();
      chartInstances.fileComparison?.dispose();
      window.removeEventListener('resize', handleResize);
    });
    
    return {
      selectedDate,
      selectedNetworkType,
      tableData,
      totalDataDifference,
      totalFileDifference,
      dataCompletionChart,
      fileComparisonChart,
      queryData,
      downloadTemplate,
      exportData,
      handleUploadSuccess,
      handleUploadError,
      beforeUpload,
      formatNumber,
      formatRate
    };
  }
};
</script>

<style scoped>
.data-comparison {
  padding: 24px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.header {
  margin-bottom: 24px;
  background: white;
  padding: 20px 24px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.operation-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.operation-group {
  display: flex;
  gap: 16px;
  align-items: center;
}

.upload-btn {
  display: inline-block;
}

.custom-button {
  font-size: 14px;
  padding: 10px 20px;
  height: auto;
  font-weight: 600;
  border-radius: 6px;
  transition: all 0.3s;
}

.custom-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

@media (max-width: 1200px) {
  .operation-row {
    flex-direction: column;
    gap: 16px;
  }

  .operation-group {
    width: 100%;
    justify-content: space-between;
  }
}

.charts-section {
  margin-bottom: 20px;
}

.chart-row {
  display: flex;
  gap: 20px;
}

.chart-box {
  flex: 1;
  background: white;
  border-radius: 4px;
  padding: 15px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.chart-title {
  font-size: 14px;
  font-weight: bold;
}

.chart-summary {
  display: flex;
  align-items: center;
  gap: 8px;
}

.summary-label {
  font-size: 14px;
  color: #666;
}

.summary-value {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
}

.chart-container {
  height: 350px;
  width: 100%;
}

.data-section {
  background: white;
  border-radius: 4px;
  padding: 20px;
}

.section-header {
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
}

:deep(.el-table th) {
  background-color: #f5f7fa !important;
}

:deep(.el-table td) {
  padding: 8px 0;
}

@media (max-width: 1200px) {
  .chart-row {
    flex-direction: column;
  }
  
  .chart-box {
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .data-comparison {
    padding: 12px;
  }

  .header {
    padding: 16px;
  }

  .operation-row {
    flex-direction: column;
    gap: 16px;
  }

  .operation-group {
    flex-direction: column;
    width: 100%;
  }

  .custom-button,
  .custom-date-picker,
  .custom-select,
  .upload-btn {
    width: 100%;
  }

  .upload-btn .el-button {
    width: 100%;
  }
}
</style>

<style>
.custom-date-picker {
  width: 180px;
}

.custom-select {
  width: 150px;
}

.custom-date-picker .el-input__inner,
.custom-select .el-input__inner {
  border-radius: 6px;
}

.el-button.custom-button {
  border-radius: 6px;
}

.el-button--primary.custom-button {
  background-color: #1890ff;
  border-color: #1890ff;
}

.el-button--primary.custom-button:hover,
.el-button--primary.custom-button:focus {
  background-color: #40a9ff;
  border-color: #40a9ff;
}

.el-button--default.custom-button {
  border-color: #d9d9d9;
  color: #595959;
}

.el-button--default.custom-button:hover,
.el-button--default.custom-button:focus {
  border-color: #40a9ff;
  color: #40a9ff;
}
</style>