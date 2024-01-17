FROM eclipse-temurin:21-alpine

VOLUME /tmp

COPY target/drones-1.0.0.jar drones.jar

ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongo:27017/drones-db", "-jar", "drones.jar"]
