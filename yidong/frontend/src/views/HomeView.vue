<template>
  <div class="home">
    <h1>Excel Analysis System</h1>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>数据上传</span>
              <el-button @click="$router.push('/upload')" type="primary">开始上传</el-button>
            </div>
          </template>
          <div class="card-content">
            上传Excel文件进行数据分析
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>数据统计</span>
              <el-button @click="$router.push('/statistics')" type="primary">查看统计</el-button>
            </div>
          </template>
          <div class="card-content">
            查看数据分析统计结果
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
.home {
  padding: 20px;
}
.box-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-content {
  padding: 20px 0;
  text-align: center;
}
</style>