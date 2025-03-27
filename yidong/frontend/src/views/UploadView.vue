<template>
  <div class="upload">
    <h1>数据上传</h1>
    <div class="upload-actions">
      <el-button type="primary" @click="downloadTemplate">
        <el-icon><Download /></el-icon>
        下载Excel模板
      </el-button>
    </div>
    <el-card class="upload-card">
      <el-upload
        class="upload-demo"
        drag
        :http-request="customUpload"
        :auto-upload="true"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :limit="1"
        accept=".xlsx,.xls"
      >
        <el-icon class="el-icon--upload"><i class="el-icon-upload"></i></el-icon>
        <div class="el-upload__text">拖拽文件到此处或 <em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">
            请上传Excel文件(.xlsx, .xls)
          </div>
        </template>
      </el-upload>
      
      <div v-if="uploadStatus" class="upload-status">
        <el-alert
          :title="uploadMessage"
          :type="uploadStatus === 'success' ? 'success' : 'error'"
          :closable="false"
          show-icon
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { Download } from '@element-plus/icons-vue'
import { dataComparisonService } from '@/services/dataComparisonService'

export default {
  components: {
    Download
  },
  name: 'UploadView',
  data() {
    return {
      uploadStatus: '',
      uploadMessage: '',
      selectedFile: null
    }
  },
  methods: {
    async downloadTemplate() {
      try {
        const data = await dataComparisonService.downloadTemplate()
        const url = window.URL.createObjectURL(new Blob([data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '数据导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败，请重试')
        console.error('下载模板失败:', error)
      }
    },
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                      file.type === 'application/vnd.ms-excel' ||
                      file.name.endsWith('.xlsx') || 
                      file.name.endsWith('.xls');
      
      if (!isExcel) {
        this.$message.error('只能上传Excel文件(.xlsx或.xls)!');
        return false;
      }
      
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$message.error('文件大小不能超过10MB!');
        return false;
      }
      
      return true;
    },
    
    handleUploadSuccess(response) {
      if (response && response.success) {
        this.uploadStatus = 'success';
        this.uploadMessage = response.message || '文件上传成功！';
        this.$message.success(this.uploadMessage);
      } else {
        this.uploadStatus = 'error';
        this.uploadMessage = response.message || '文件上传失败！';
        this.$message.error(this.uploadMessage);
      }
    },
    
    handleUploadError(error) {
      this.uploadStatus = 'error';
      const errorMessage = error.response?.data?.message || '文件上传失败，请重试！';
      this.uploadMessage = errorMessage;
      this.$message.error(errorMessage);
    },

    async customUpload({ file }) {
      this.uploadStatus = '';
      this.uploadMessage = '';
      
      // 创建FormData对象
      const formData = new FormData();
      formData.append('file', file);
      
      console.log('正在上传文件:', file.name, '类型:', file.type, '大小:', file.size);
      
      try {
        const response = await dataComparisonService.importExcel(formData);
        console.log('上传成功，响应:', response);
        this.handleUploadSuccess(response);
        return response;
      } catch (error) {
        console.error('上传失败，错误:', error);
        this.handleUploadError(error);
        throw error;
      }
    }
  }
}
</script>

<style>
.upload-actions {
  margin-bottom: 20px;
  text-align: right;
}
.upload {
  padding: 20px;
}
.upload-card {
  max-width: 600px;
  margin: 0 auto;
}
.upload-status {
  margin-top: 20px;
}
</style>