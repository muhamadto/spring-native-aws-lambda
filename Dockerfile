FROM ghcr.io/muhamadto/spring-native-amazonlinux2-base-java17:latest AS spring-native-aws-lambda-builder-java17

RUN ./mvnw -ntp -Pnative package -DskipTests --file spring-native-aws-lambda-function/pom.xml