#!/bin/bash

echo "Starting both backend and frontend servers..."

# Start backend in background
./scripts/start-backend.sh &
BACKEND_PID=$!

# Start frontend in background
./scripts/start-frontend.sh &
FRONTEND_PID=$!

# Function to handle script termination
function cleanup {
  echo "Stopping servers..."
  kill $BACKEND_PID
  kill $FRONTEND_PID
  exit
}

# Register the cleanup function for when script receives SIGINT (Ctrl+C)
trap cleanup SIGINT

echo "Both servers are running. Press Ctrl+C to stop both servers."

# Wait for both processes
wait