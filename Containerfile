FROM docker.io/eclipse-temurin:25.0.1_8-jdk AS builder
WORKDIR /builder
COPY src ./src
COPY pom.xml ./pom.xml
COPY mvnw ./mvnw
COPY .mvn ./.mvn
RUN ./mvnw -DskipTests package
RUN cp target/server-0.0.1-SNAPSHOT.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM docker.io/eclipse-temurin:25.0.1_8-jre
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]