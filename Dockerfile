FROM centos:7 AS spring-native-aws-lambda-builder

ENV GRAALVM_VERSION=21.1.0
ENV FILE_NAME=graalvm-ce-java11-linux-amd64-${GRAALVM_VERSION}.tar.gz
ENV JAVA_HOME=./graalvm-ce-java11-${GRAALVM_VERSION}

RUN mkdir -p ~/.m2/
RUN echo "<settings xmlns=\"http://maven.apache.org/SETTINGS/1.0.0\" \
            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \
            xsi:schemaLocation=\"http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd\"> \
            <profiles> \
              <profile> \
                <id>spring-native-demo</id> \
                <repositories> \
                  <repository> \
                    <id>spring-releases</id> \
                    <name>Spring Releases</name> \
                    <url>https://repo.spring.io/release</url> \
                  </repository> \
                </repositories> \
                <pluginRepositories> \
                  <pluginRepository> \
                    <id>spring-releases</id> \
                    <name>Spring Releases</name> \
                    <url>https://repo.spring.io/release</url> \
                  </pluginRepository> \
                </pluginRepositories> \
              </profile> \
            </profiles> \
            <activeProfiles> \
              <activeProfile>spring-native-demo</activeProfile> \
            </activeProfiles> \
          </settings>" >> ~/.m2/settings.xml

RUN yum -y update
RUN yum install -y wget tar gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran \
    less libcurl-devel openssl openssl-devel readline-devel xz-devel \
    zlib-devel glibc-static libcxx libcxx-devel llvm-toolset-7 zlib-static
RUN rm -rf /var/cache/yum

COPY . app
WORKDIR app

RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAALVM_VERSION}/${FILE_NAME}
RUN tar zxvf ${FILE_NAME}
RUN rm -f ${FILE_NAME}
RUN ${JAVA_HOME}/bin/gu install native-image

RUN ./mvnw -ntp package -Pnative-image

