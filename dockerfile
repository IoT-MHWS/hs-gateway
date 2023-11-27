FROM openjdk:17-alpine

COPY "build/libs/hs-gateway-*.jar" application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
