# 1단계: 빌드 스테이지 (Gradle 포함)
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
# 테스트 제외 후 실행 가능한 JAR 빌드
RUN gradle build -x test

# 2단계: 실행 스테이지 (최소한의 JRE만 포함)
FROM openjdk:17-slim
WORKDIR /app

# 빌드 스테이지에서 생성된 jar 복사
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]