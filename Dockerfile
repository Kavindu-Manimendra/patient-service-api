FROM maven:3.9.9-eclipse-temurin-21 AS builder
#Uses an official Maven 3.9.9 image with Temurin JDK 21 installed.
#This stage will compile and package your Java application.

WORKDIR /app
#Sets the working directory to /app inside the container. All subsequent commands will run from this directory.

COPY pom.xml .
#Copies only the pom.xml file into the container’s /app directory.
#This allows Maven to download dependencies before copying the full source code (to leverage caching).

RUN mvn dependency:go-offline -B
#download and cache all dependencies defined in the pom.xml.
#Speeds up future builds since dependencies don’t need to be downloaded again unless pom.xml changes.

COPY src ./src
#Copies the project’s src folder (Java source code) into /app/src.

RUN mvn clean package
#Compiles the source code and packages it into a JAR file using Maven.
#Output JAR file will typically be placed in /app/target/.

FROM openjdk:17-jdk AS runner
#Uses OpenJDK 17 as the runtime environment (lighter than Maven image).

WORKDIR /app
#Sets the working directory to /app in the runtime container.

COPY --from=builder ./app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar
#Copies the packaged JAR file from the builder stage (/app/target/...) into the runtime container’s /app/app.jar.

EXPOSE 5050
#application will listen on port 5050.

ENTRYPOINT ["java", "-jar", "app.jar"]