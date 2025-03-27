#!/bin/bash

# 打印步骤信息的函数
print_step() {
  echo "============================================"
  echo "  $1"
  echo "============================================"
}

# 1. 构建前端
print_step "构建前端项目"
cd frontend
echo "安装前端依赖..."
npm install
echo "构建前端生产版本..."
npm run build
if [ $? -ne 0 ]; then
  echo "前端构建失败！"
  exit 1
fi
cd ..

# 2. 构建后端
print_step "构建后端项目"
cd backend
echo "打包Spring Boot应用..."
mvn clean package
if [ $? -ne 0 ]; then
  echo "后端构建失败！"
  exit 1
fi
cd ..

# 3. 复制最终JAR包到根目录
print_step "完成构建"
cp backend/target/data-comparison-0.0.1-SNAPSHOT.jar ./比对统计系统.jar
echo "构建完成！JAR包已生成: 比对统计系统.jar"
echo "运行方式: java -jar 比对统计系统.jar"