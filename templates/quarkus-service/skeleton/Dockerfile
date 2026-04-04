####################################
# Stage 1 — Build
####################################
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B -q
COPY src ./src
RUN mvn clean package -DskipTests -B -q

####################################
# Stage 2 — Runtime
####################################
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /work
RUN addgroup -S tradeflow && adduser -S -G tradeflow tradeflow
COPY --from=builder --chown=tradeflow:tradeflow /app/target/quarkus-app /work/quarkus-app
USER tradeflow
EXPOSE ${{ values.port }}
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /work/quarkus-app/quarkus-run.jar"]