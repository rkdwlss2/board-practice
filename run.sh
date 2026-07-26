#!/bin/bash

# 1. EC2 Public IP 조회
EC2_PUBLIC_IP=$(curl -s ifconfig.me)

# 2. 조회한 IP 확인 출력
echo "Current Public IP: http://${EC2_PUBLIC_IP}:5173"

# 3. Spring Boot 실행 시 환경변수로 주입
PUBLIC_IP="http://${EC2_PUBLIC_IP}:5173" java -jar my-app.jar