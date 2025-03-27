import axios from 'axios'

const API_URL = '/api/data-comparison'

export const dataComparisonService = {
  // 获取数据列表
  async getData(params) {
    try {
      const response = await axios.get(`${API_URL}/statistics`, {
        params: {
          date: params.date,
          networkType: params.networkType
        }
      });
      return response.data;
    } catch (error) {
      console.error('获取数据列表失败:', error);
      throw error;
    }
  },
  
  // 获取统计数据
  async getStatistics(date, networkType) {
    try {
      const response = await axios.get(`${API_URL}/statistics`, {
        params: {
          date,
          networkType
        }
      });
      return response.data;
    } catch (error) {
      console.error('获取统计数据失败:', error);
      throw error;
    }
  },
  
  // 下载模板
  async downloadTemplate() {
    try {
      const response = await axios.get(`${API_URL}/template`, {
        responseType: 'blob'
      });
      return response.data;
    } catch (error) {
      console.error('下载模板失败:', error);
      throw error;
    }
  },
  
  // 导出Excel
  async exportExcel(date, networkType) {
    try {
      const response = await axios.get(`${API_URL}/export`, {
        params: {
          date,
          networkType
        },
        responseType: 'blob'
      });
      return response.data;
    } catch (error) {
      console.error('导出Excel失败:', error);
      throw error;
    }
  }
}