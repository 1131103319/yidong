#!/bin/bash

echo "Stopping MySQL container..."
CONTAINER_NAME="excel_analysis_mysql"

if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    docker stop $CONTAINER_NAME
    echo "MySQL container stopped."
else
    echo "MySQL container is not running."
fi