#!/bin/bash

# 设置基本URL
BASE_URL="http://localhost:8081/api/data-comparison"

echo "===== 测试 Excel 模板下载接口 ====="
curl -v -o template.xlsx "${BASE_URL}/template"
echo -e "\n"

echo "===== 测试数据统计接口 ====="
curl -v "${BASE_URL}/statistics?date=2023-01-01&networkType=4G"
echo -e "\n"

echo "===== 测试数据统计接口 - 缺少参数 ====="
curl -v "${BASE_URL}/statistics"
echo -e "\n"

echo "===== 测试数据统计接口 - 只有日期 ====="
curl -v "${BASE_URL}/statistics?date=2023-01-01"
echo -e "\n"

echo "===== 测试数据统计接口 - 只有网络类型 ====="
curl -v "${BASE_URL}/statistics?networkType=4G"
echo -e "\n"

echo "===== 测试数据导出接口 ====="
curl -v -o export.xlsx "${BASE_URL}/export?date=2023-01-01&networkType=4G"
echo -e "\n"

echo "===== 测试文件上传接口 - 使用示例Excel文件 ====="
# 注意：需要有一个实际的Excel文件用于测试
if [ -f "test.xlsx" ]; then
    curl -v -F "file=@test.xlsx" "${BASE_URL}/upload"
else
    echo "警告：test.xlsx 文件不存在，无法测试上传接口"
fi
echo -e "\n"

echo "===== 测试文件上传接口 - 使用示例CSV文件 ====="
# 注意：需要有一个实际的CSV文件用于测试
if [ -f "test.csv" ]; then
    curl -v -F "file=@test.csv" "${BASE_URL}/upload"
else
    echo "警告：test.csv 文件不存在，无法测试上传接口"
fi
echo -e "\n"

echo "===== 测试数据导入接口 ====="
if [ -f "test.xlsx" ]; then
    curl -v -F "file=@test.xlsx" "${BASE_URL}/import"
else
    echo "警告：test.xlsx 文件不存在，无法测试导入接口"
fi
echo -e "\n"

echo "测试完成"