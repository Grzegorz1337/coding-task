FROM gradle:8.0.2-jdk19-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:19-alpine
MAINTAINER Kamil Grzeczkowski "kamil.grzeczkowski@gmail.com"

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/coding-task-application.jar

ENTRYPOINT ["java"]
CMD ["-jar", "/app/coding-task-application.jar"]