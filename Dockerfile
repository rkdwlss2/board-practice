# 1단계: 빌드 스테이지 (Gradle 8.5 + JDK 17 적용)
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .

# --no-daemon 옵션을 주어 EC2 메모리 부족 방지
RUN gradle build -x test --no-daemon

# 2단계: 실행 스테이지 (JRE 17)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]