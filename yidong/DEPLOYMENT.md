# 比对统计系统部署指南

本文档提供了比对统计系统的打包和部署步骤。

## 一体化打包（前后端打包在一起）

### 前提条件

- JDK 8 或更高版本
- Maven 3.6 或更高版本
- Node.js 14 或更高版本
- npm 6 或更高版本
- MySQL 8.0 或更高版本

### 打包步骤

1. **使用自动化脚本打包**

   ```bash
   # 确保脚本有执行权限
   chmod +x build-all.sh
   
   # 运行打包脚本
   ./build-all.sh
   ```

   脚本会自动完成以下操作：
   - 构建前端项目并将输出放到后端的 resources/static 目录
   - 构建后端 Spring Boot 项目
   - 生成最终的 JAR 包：比对统计系统.jar

2. **手动打包步骤**（如果不使用脚本）

   ```bash
   # 构建前端
   cd frontend
   npm install
   npm run build
   cd ..
   
   # 构建后端
   cd backend
   mvn clean package
   cd ..
   ```

### 部署步骤

1. **准备数据库**

   ```sql
   CREATE DATABASE IF NOT EXISTS excel_analysis;
   ```

2. **配置数据库连接**

   创建一个 `application.properties` 文件，与 JAR 包放在同一目录：

   ```properties
   # 数据库配置
   spring.datasource.url=jdbc:mysql://localhost:3306/excel_analysis?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=你的数据库用户名
   spring.datasource.password=你的数据库密码
   
   # 其他配置
   server.port=8081
   ```

3. **运行应用**

   ```bash
   java -jar 比对统计系统.jar
   ```

4. **访问应用**

   打开浏览器，访问：`http://localhost:8081`

## 系统要求

- **内存**: 最低 2GB，推荐 4GB
- **存储**: 最低 1GB 可用空间
- **网络**: 支持 HTTP/HTTPS 协议

## 常见问题

1. **应用无法启动**
   - 检查 JDK 版本是否兼容
   - 检查数据库连接配置是否正确
   - 查看日志文件了解详细错误信息

2. **无法连接数据库**
   - 确认数据库服务是否正在运行
   - 检查数据库用户名和密码是否正确
   - 确认数据库端口是否被防火墙阻止

3. **前端页面无法加载**
   - 检查浏览器控制台是否有错误信息
   - 确认应用服务器是否正确处理静态资源

## 维护与更新

更新系统时，只需替换 JAR 文件并重启应用即可。建议在更新前备份数据库和配置文件。