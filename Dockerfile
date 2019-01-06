FROM maven:3.6.0-jdk-8-alpine as MAVEN_TOOL_CHAIN

COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Dmaven.test.skip

FROM openjdk:8-jdk-alpine
EXPOSE 8082
ADD docker/config /
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/cake-redux-1.0.TDC-jar-with-dependencies.jar /cake-redux.jar
cmd java ${JAVA_OPTS} -jar /cake-redux.jar /config
