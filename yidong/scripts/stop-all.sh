#!/bin/bash

echo "Stopping all running servers..."

# Find and kill backend process
BACKEND_PID=$(ps aux | grep "[s]pring-boot:run" | awk '{print $2}')
if [ ! -z "$BACKEND_PID" ]; then
  echo "Stopping backend (PID: $BACKEND_PID)..."
  kill $BACKEND_PID
fi

# Find and kill frontend process
FRONTEND_PID=$(ps aux | grep "[n]pm run serve" | awk '{print $2}')
if [ ! -z "$FRONTEND_PID" ]; then
  echo "Stopping frontend (PID: $FRONTEND_PID)..."
  kill $FRONTEND_PID
fi

echo "All servers stopped."