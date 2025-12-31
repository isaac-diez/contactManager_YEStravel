# 1. BUILD: Optimizamos caché descargando dependencias primero
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# 2. RUNTIME: JRE ligero y límites de memoria para Render
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Usamos la variable de entorno PORT que Render inyecta automáticamente
# Limitamos la memoria a 400MB para no exceder los 512MB de la capa gratuita
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Xmx400m", "-jar", "app.jar", "--spring.profiles.active=prod"]