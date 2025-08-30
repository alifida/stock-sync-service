FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/stock-sync-service-0.0.1-SNAPSHOT.jar app.jar
# Copy the CSV file to the expected location
COPY docker/vendor-b/stock.csv /tmp/vendor-b/stock.csv
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]