#!/bin/bash


echo "📦 Compiling Java files..."
javac -cp .:lib/mysql-connector-j-8.0.33.jar Server/*.java Client/*.java
if [ $? -ne 0 ]; then
  echo "❌ Compilation failed!"
  exit 1
fi


echo "🚦 Starting RMI registry on port 2001..."
rmiregistry  &
RMI_PID=$!
sleep 1


echo "🖥️ Starting RMI server..."
java -cp .:lib/mysql-connector-j-8.0.33.jar Server.ServerMain &
SERVER_PID=$!
sleep 2


echo "👤 Choose client to run:"
echo "1) CabBookingClient (CLI)"
echo "2) CabBookingGUI (GUI)"
read -p "Enter choice [1-2]: " client_choice

if [ "$client_choice" = "1" ]; then
  java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingClient
elif [ "$client_choice" = "2" ]; then
  java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingGUI
else
  echo "❌ Invalid choice. Exiting."
fi


echo "🛑 Stopping RMI registry and server..."
kill $RMI_PID $SERVER_PID 2>/dev/null

echo "✅ Done."
