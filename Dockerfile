FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar archivos de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Dar permisos de ejecución a gradlew
RUN chmod +x gradlew

# Compilar la aplicación
RUN ./gradlew clean build -x test

# Etapa de producción
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR compilado
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer puerto (informative)
EXPOSE 8080

# Comando de inicio / Puerto Render
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]