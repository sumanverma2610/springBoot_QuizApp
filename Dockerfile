# ---- Build stage ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy only what's needed to resolve dependencies first, so Docker can
# cache this layer and skip re-downloading everything on every build.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Now copy the actual source and build the jar.
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ---- Run stage ----
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render assigns a port via the PORT env var at runtime - the app is
# configured (see application.properties) to listen on it.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]