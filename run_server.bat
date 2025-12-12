@echo off
echo ========================================
echo  INVENTORY MANAGEMENT SYSTEM (Node.js)
echo ========================================
echo.
echo [1/3] Installing dependencies...
call npm install
echo.
echo [2/3] Checking MySQL Configuration...
echo Please ensure you have edited db.js with your MySQL password!
echo.
echo [3/3] Starting Server...
node server.js
pause
