FROM openjdk:8

RUN mkdir -p /var/identity-authorization

RUN chmod -R 777 /var/identity-authorization

WORKDIR /var/identity-authorization

COPY ./target/authorization-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8403

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait

RUN chmod +x /wait

CMD ["java", "-jar", "app.jar"]