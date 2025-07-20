FROM docker.io/alpine/java:21-jdk AS builder
COPY target/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --launcher --destination layers

FROM docker.io/alpine/java:21-jre
COPY --from=builder layers/dependencies/ ./
COPY --from=builder layers/snapshot-dependencies/ ./
COPY --from=builder layers/spring-boot-loader/ ./
COPY --from=builder layers/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]