FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
ADD target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]