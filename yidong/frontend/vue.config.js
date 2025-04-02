const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  // 修改构建输出目录到后端的static目录
  outputDir: '../backend/src/main/resources/static',
  // 确保资源路径正确
  publicPath: './',
  devServer: {
    port: 12110,
    proxy: {
      '/api': {
        target: 'http://localhost:12111',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  }
})