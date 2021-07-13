FROM amazoncorretto:11-alpine-jdk
MAINTAINER serdar burak guneri
COPY target/msgboard-0.0.1-SNAPSHOT.jar msgboard-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/msgboard-0.0.1-SNAPSHOT.jar"]