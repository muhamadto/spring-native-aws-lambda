FROM ghcr.io/muhamadto/spring-native-amazonlinux2-base:17-amazonlinux2

COPY --chown=worker:ci . /home/worker/

RUN ./mvnw -ntp -Pnative package -DskipTests --file spring-native-aws-lambda-function/pom.xml