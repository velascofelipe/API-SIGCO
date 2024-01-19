# Utiliza una imagen base de Maven para construir la aplicación
FROM maven:3.8.4-openjdk-17 AS builder

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo POM y descarga las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto de la aplicación
COPY src src

# Construye la aplicación
RUN mvn package -DskipTests

# Utiliza una imagen base de Java para la ejecución
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR construido por Maven desde la fase de construcción
COPY --from=builder /app/target/nombre-de-tu-archivo.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación al iniciar el contenedor
CMD ["java", "-jar", "app.jar"]
