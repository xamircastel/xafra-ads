# Usar imagen base de OpenJDK 8
FROM openjdk:8-jre-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar el WAR ya compilado
COPY target/ads-xafra.war app.war

# Crear directorio de logs
RUN mkdir -p /logs

# Exponer puerto
EXPOSE 8080

# Variables de entorno optimizadas para Cloud Run
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -Djava.awt.headless=true -Dspring.main.lazy-initialization=true"

# Comando para ejecutar la aplicación con lazy initialization para startup más rápido
CMD ["sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.war --server.port=$PORT"]
