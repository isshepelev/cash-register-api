FROM maven:3.9.9-amazoncorretto-17-alpine AS build
WORKDIR /app
COPY pom.xml .
#RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean verify

FROM openjdk:17-oracle

ENV JAVA_OPTS="-Xmx256m"

COPY --from=build /app/target/Auto*.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN apk add --no-cache tzdata; \
  cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime; \
  echo "Europe/Moscow" >  /etc/timezone; \
  chmod 755 /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]