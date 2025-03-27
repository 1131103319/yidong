<template>
  <div class="statistics" v-loading="loading">
    <div class="header-section">
      <div class="title">
        <i class="el-icon-data-analysis"></i> 数据统计
      </div>
      <div class="controls">
        <el-date-picker
          v-model="selectedDate"
          type="date"
          value-format="YYYY-MM-DD"
          format="YYYY-MM-DD"
          :clearable="true"
          :editable="false"
          placeholder="请选择日期"
          @change="handleDateChange">
        </el-date-picker>
        <el-select v-model="selectedType" placeholder="请选择网络类型" @change="handleTypeChange">
          <el-option
            v-for="item in networkTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <el-button type="primary" @click="fetchData" :loading="loading">查询</el-button>
      </div>
    </div>

    <div class="data-section" v-if="!loading">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>数据统计</span>
            <span class="data-count">共 {{ totalRecords }} 条记录</span>
          </div>
        </template>
        
        <!-- 数据统计表格 -->
        <el-table 
          :data="tableData" 
          style="width: 100%" 
          border 
          v-loading="loading"
          :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
        >
          <!-- 序号列 -->
          <el-table-column 
            type="index" 
            label="序号" 
            width="70" 
            align="center"
          />
          
          <!-- 日期列 -->
          <el-table-column 
            prop="date" 
            label="日期" 
            width="120" 
            align="center"
          />
          
          <!-- 网络类型列 -->
          <el-table-column 
            prop="networkType" 
            label="网络类型" 
            width="120" 
            align="center"
          />
          
          <!-- 业务类型列 -->
          <el-table-column 
            prop="business" 
            label="业务类型" 
            width="150" 
            align="center"
          />
          
          <!-- 入库量列 -->
          <el-table-column 
            prop="inputAmount1" 
            label="入库量" 
            width="120" 
            align="center"
          >
            <template #default="scope">
              {{ formatNumber(scope.row.inputAmount1) }}
            </template>
          </el-table-column>
          
          <!-- 接收量列 -->
          <el-table-column 
            prop="inputAmount2" 
            label="接收量" 
            width="120" 
            align="center"
          >
            <template #default="scope">
              {{ formatNumber(scope.row.inputAmount2) }}
            </template>
          </el-table-column>
          
          <!-- 用户名解析回填率列 -->
          <el-table-column 
            prop="phoneNullRate" 
            label="用户名解析回填率" 
            width="150" 
            align="center"
          >
            <template #default="scope">
              {{ formatRate(scope.row.phoneNullRate) }}
            </template>
          </el-table-column>
          
          <!-- 域名回填率列 -->
          <el-table-column 
            prop="domainNullRate" 
            label="域名回填率" 
            width="120" 
            align="center"
          >
            <template #default="scope">
              {{ formatRate(scope.row.domainNullRate) }}
            </template>
          </el-table-column>
          
          <!-- 目的IP回填率列 -->
          <el-table-column 
            prop="destIpNullRate" 
            label="目的IP回填率" 
            width="140" 
            align="center"
          >
            <template #default="scope">
              {{ formatRate(scope.row.destIpNullRate) }}
            </template>
          </el-table-column>
          
          <!-- 目的端口回填率列 -->
          <el-table-column 
            prop="destPortNullRate" 
            label="目的端口回填率" 
            width="140" 
            align="center"
          >
            <template #default="scope">
              {{ formatRate(scope.row.destPortNullRate) }}
            </template>
          </el-table-column>
          
          <!-- 公网IP回填率列 -->
          <el-table-column 
            prop="sourceIpNullRate" 
            label="公网IP回填率" 
            width="140" 
            align="center"
          >
            <template #default="scope">
              {{ formatRate(scope.row.sourceIpNullRate) }}
            </template>
          </el-table-column>
          
          <!-- 源端口回填率列 -->
          <el-table-column 
            prop="sourcePortNullRate" 
            label="源端口回填率" 
            width="140" 
            align="center"
          >
            <template #default="scope">
              {{ formatRate(scope.row.sourcePortNullRate) }}
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 无数据提示 -->
        <div v-if="tableData.length === 0" class="no-data">
          <el-empty description="暂无数据"></el-empty>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { dataComparisonService } from '@/services/dataComparisonService';

export default defineComponent({
  name: 'StatisticsView',
  
  setup() {
    const loading = ref(false);
    const tableData = ref([]);
    const totalRecords = ref(0);
    const selectedDate = ref('');
    const selectedType = ref('');
    
    const networkTypes = [
      { value: '4G', label: '4G网络' },
      { value: '5G', label: '5G网络' },
      { value: 'WIFI', label: 'WIFI网络' }
    ];
    
    const handleDateChange = (value) => {
      selectedDate.value = value;
    };
    
    const handleTypeChange = (value) => {
      selectedType.value = value;
    };
    
    const fetchData = async () => {
      if (!selectedDate.value) {
        ElMessage.warning('请选择日期');
        return;
      }
      
      if (!selectedType.value) {
        ElMessage.warning('请选择网络类型');
        return;
      }
      
      loading.value = true;
      try {
        const result = await dataComparisonService.getDataStatistics(
          selectedDate.value,
          selectedType.value
        );
        
        tableData.value = result.data;
        totalRecords.value = result.total || 0;
        
      } catch (error) {
        console.error('获取数据失败:', error);
        ElMessage.error('获取数据失败: ' + (error.message || '未知错误'));
      } finally {
        loading.value = false;
      }
    };
    
    const formatNumber = (value) => {
      return value ? value.toLocaleString() : '0';
    };
    
    const formatRate = (value) => {
      if (value === null || value === undefined) return '0%';
      return (value * 100).toFixed(2) + '%';
    };

    onMounted(() => {
      fetchData();
    });

    return {
      loading,
      tableData,
      totalRecords,
      selectedDate,
      selectedType,
      networkTypes,
      handleDateChange,
      handleTypeChange,
      fetchData,
      formatNumber,
      formatRate
    };
  }
});
</script>

<style scoped>
.statistics {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title {
  font-size: 24px;
  font-weight: bold;
}

.controls {
  display: flex;
  gap: 10px;
}

.data-section {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.data-count {
  font-size: 14px;
  color: #909399;
}

.no-data {
  margin-top: 20px;
  text-align: center;
}
</style>