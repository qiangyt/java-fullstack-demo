docker-compose up -d --build --remove-orphans
@REM docker compose up --build -d --remove-orphans

timeout /t 5 /nobreak

start http://localhost:8000