FROM amazonlinux:2 AS spring-native-aws-lambda-builder

ENV GRAALVM_VERSION=22.1.0
ENV FILE_NAME=graalvm-ce-java17-linux-amd64-${GRAALVM_VERSION}.tar.gz
ENV JAVA_HOME=./graalvm-ce-java17-${GRAALVM_VERSION}

RUN yum -y update
RUN yum install -y wget tar gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran \
    less libcurl-devel openssl openssl-devel readline-devel xz-devel \
    zlib-devel glibc-static libcxx libcxx-devel llvm-toolset-7 zlib-static
RUN rm -rf /var/cache/yum

COPY . /app
WORKDIR /app

RUN mkdir -p ~/.m2/
RUN cp /app/settings-spring-native.xml ~/.m2/settings.xml

RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAALVM_VERSION}/${FILE_NAME}
RUN tar zxvf ${FILE_NAME}
RUN rm -f ${FILE_NAME}
RUN ${JAVA_HOME}/bin/gu install native-image

RUN ./mvnw -ntp package -Pnative