# Excel数据分析系统

这是一个基于Spring Boot和Vue.js的Excel数据分析系统，用于上传、处理和分析Excel数据。

## 项目结构

```
.
├── backend/                # 后端项目
│   ├── src/                # 源代码
│   │   ├── main/
│   │   │   ├── java/       # Java代码
│   │   │   └── resources/  # 资源文件
│   │   └── test/           # 测试代码
│   └── pom.xml             # Maven配置
└── frontend/               # 前端项目
    ├── public/             # 静态资源
    ├── src/                # 源代码
    │   ├── assets/         # 资源文件
    │   ├── components/     # 组件
    │   ├── views/          # 视图
    │   ├── router/         # 路由
    │   ├── store/          # 状态管理
    │   └── services/       # 服务
    ├── package.json        # npm配置
    └── vue.config.js       # Vue配置
```

## 后端技术栈

- Spring Boot 2.7.x
- Spring Data JPA
- MySQL 8.0
- Apache POI (Excel处理)
- Lombok

## 前端技术栈

- Vue.js 3.x
- Vue Router
- Vuex
- Element Plus
- Axios
- ECharts

## 如何运行

### 后端

1. 确保已安装JDK 8或更高版本
2. 确保已安装MySQL 8.0
3. 在`backend/src/main/resources/application.properties`中配置数据库连接
4. 进入backend目录，运行：`mvn spring-boot:run`

### 前端

1. 确保已安装Node.js 14或更高版本
2. 进入frontend目录，运行：
   ```
   npm install
   npm run serve
   ```

## 访问应用

- 前端：http://localhost:8080
- 后端API：http://localhost:8081