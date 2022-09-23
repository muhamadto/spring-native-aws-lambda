FROM ghcr.io/muhamadto/spring-native-aws-lambda-builder-java17:latest AS spring-native-aws-lambda-builder-java17

COPY . /opt/build

RUN ./mvnw -ntp -Pnative package -DskipTests