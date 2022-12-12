FROM ghcr.io/muhamadto/spring-native-amazonlinux2-base-java17:latest AS spring-native-aws-lambda-builder-java17

COPY --chown=${USER_NAME}:${GROUP_NAME} . /opt/build

RUN ./mvnw -ntp -Pnative package -DskipTests --file spring-native-aws-lambda-function/pom.xml