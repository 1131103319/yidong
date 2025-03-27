<template>
  <div id="app">
    <el-container>
      <el-header>
        <h1 class="app-title">比对统计系统</h1>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
      <el-footer>
        <p>© 2023 比对统计系统</p>
      </el-footer>
    </el-container>
  </div>
</template>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.el-header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
}

.app-title {
  font-size: 24px;
  font-weight: 500;
  margin: 0;
  letter-spacing: 1px;
  color: #409EFF;
}

.el-main {
  background-color: #f0f2f5;
  min-height: calc(100vh - 120px);
}

.el-footer {
  background-color: #f5f5f5;
  color: #666;
  line-height: 60px;
  font-size: 14px;
}
</style>