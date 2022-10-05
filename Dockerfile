FROM ghcr.io/muhamadto/spring-native-amazonlinux2-base-java17:latest AS spring-native-aws-lambda-builder-java17

COPY . /opt/build

RUN ./mvnw -ntp -Pnative package -DskipTests
