#!/bin/bash

echo "Starting MySQL container..."

# Check if container already exists
CONTAINER_NAME="excel_analysis_mysql"
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)" ]; then
        # Cleanup
        docker rm $CONTAINER_NAME
    else
        echo "MySQL container is already running!"
        exit 0
    fi
fi

# Start MySQL container
docker run --name $CONTAINER_NAME \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=123456 \
    -e MYSQL_DATABASE=excel_analysis \
    -d mysql:8.0

echo "Waiting for MySQL to start..."
sleep 10

echo "MySQL container is ready!"