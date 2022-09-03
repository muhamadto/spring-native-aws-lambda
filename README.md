# spring-native-aws-lambda [![CodeQL](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/codeql-analysis.yml) [![contributors](./contributors.svg)](https://github.com/muhamadto/spring-native-aws-lambda/graphs/contributors)


## Important Dependencies Versions
1. Java Version: 17
2. Spring Native Version: 0.12.1
3. Spring Cloud Version: 2021.0.3
4. AWS Lambda Events Version: 3.11.0

## Building and Running

### Locally

1. Run the following commands
    ```shell
    $ ./mvnw -ntp clean package -Pnative --settings ./settings-spring-native.xml
    $ target/spring-native-aws-lambda
    ```
   The service starts in less than 200 ms
   ```shell
   2021-05-28 11:05:29.221  INFO 15449 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : Started SpringNativeAwsLambdaApplication in 0.024 seconds (JVM running for 0.027)
   Started
   2021-05-28 11:05:29.225  INFO 15449 --- [ server-startup] nctionEndpointInitializer$ServerListener : HTTP server started on port: 8080
   2021-05-28 11:05:29.225  INFO 15449 --- [ server-startup] nctionEndpointInitializer$ServerListener : JVM running for 31.0ms
   ```
2. Make a call
    ```shell
   $ curl --location --request POST 'http://localhost:8080/exampleFunction' \
   --header 'Content-Type: application/json' \
   --data-raw '{
        "name": "CoffeeBeans"
      }'
    ```
   The service responds
   ```json
   [
       {
           "name": "CoffeeBeans",
           "saved": true
       }
   ]
   ```

### AWS environment

1. Run the following commands locally
    ```shell
    $ mkdir target
    
    $ docker build --no-cache \
      --tag spring-native-aws-lambda:0.0.1-SNAPSHOT \
      --file Dockerfile .

    $ docker ps -a  
    $ docker rm spring-native-aws-lambda                                                                                                                                
    $ docker run --name spring-native-aws-lambda spring-native-aws-lambda:0.0.1-SNAPSHOT                                                            
    $ docker cp spring-native-aws-lambda:app/target/ .
    ```
2. Upload the *spring-native-aws-lambda-0.0.1-SNAPSHOT-native-zip.zip* file to aws lambda,
3. Set the handler to `exampleFunction`
4. Test,
5. Et voila! It runs with 500 ms for cold start.

# Maven Repository

This project uses experimental dependencies from Spring. 

* One way to pul them is to add `repositories` and `pluginRepositories` elements to `pom.xml`. 

* An alternative add them to `settings.xml` as following

```xml

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <profiles>
    <profile>
      <id>spring-native-demo</id>
      <repositories>
        <repository>
          <id>spring-releases</id>
          <name>Spring Releases</name>
          <url>https://repo.spring.io/release</url>
        </repository>
      </repositories>

      <pluginRepositories>
        <pluginRepository>
          <id>spring-releases</id>
          <name>Spring Releases</name>
          <url>https://repo.spring.io/release</url>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>spring-native-demo</activeProfile>
  </activeProfiles>
</settings>
```
