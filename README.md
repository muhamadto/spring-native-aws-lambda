# spring-native-aws-lambda

[![CodeQL](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/codeql-analysis.yml)
[![Build](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/build.yml/badge.svg)](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/build.yml)
[![Deploy to AWS](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/release.yml/badge.svg?branch=feature-java17-cdk)](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/release.yml)

[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=muhamadto_spring-native-aws-lambda&metric=alert_status)](https://sonarcloud.io/dashboard?id=muhamadto_spring-native-aws-lambda)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=muhamadto_spring-native-aws-lambda&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=muhamadto_spring-native-aws-lambda)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=muhamadto_spring-native-aws-lambda&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=muhamadto_spring-native-aws-lambda)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=muhamadto_spring-native-aws-lambda&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=muhamadto_spring-native-aws-lambda)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=muhamadto_spring-native-aws-lambda&metric=bugs)](https://sonarcloud.io/summary/new_code?id=muhamadto_spring-native-aws-lambda)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=muhamadto_spring-native-aws-lambda&metric=coverage)](https://sonarcloud.io/component_measures?id=muhamadto_spring-native-aws-lambda&metric=new_coverage&view=list)

| Component     | Version  |
|---------------|----------|
| JDK           | 20       |
| Spring Cloud  | 2022.0.1 |
| Spring Boot   | 3.1.2    |

## Test

```bash
$ sdk use java 22.2.r17-grl
$ ./mvnw -ntp clean verify -U --settings ./settings-spring.xml
```

## Building and Running

### Locally

1. Set `spring.main.web-application-type` to `servlet` for local development
2. Run the following commands
    ```shell
    $ ./mvnw -ntp clean package -U -Pnative --settings ./settings-spring.xml
    $ ./spring-native-aws-lambda-function/target/spring-native-aws-lambda-function
    ```
   The service starts in less than 100 ms
   ```shell
   2022-12-07 02:56:51.706  INFO 42417 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : Starting SpringNativeAwsLambdaApplication using Java 17.0.4
   2022-12-07 02:56:51.706  INFO 42417 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : No active profile set, falling back to 1 default profile: "default"
   2022-12-07 02:56:51.723  INFO 42417 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
   2022-12-07 02:56:51.724  INFO 42417 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
   2022-12-07 02:56:51.724  INFO 42417 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.68]
   2022-12-07 02:56:51.733  INFO 42417 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
   2022-12-07 02:56:51.733  INFO 42417 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 27 ms
   2022-12-07 02:56:51.761  INFO 42417 --- [           main] o.s.c.f.web.mvc.FunctionHandlerMapping   : FunctionCatalog: org.springframework.cloud.function.context.catalog.BeanFactoryAwareFunctionRegistry@7efd575
   2022-12-07 02:56:51.763  INFO 42417 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
   2022-12-07 02:56:51.763  INFO 42417 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : Started SpringNativeAwsLambdaApplication in 0.084 seconds (JVM running for 0.087)
   ```
3. Make a call
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

### Github action

#### Initial setup for Github action to work

1. Create
   an [Identity providers in AWS for github actions](https://www.eliasbrange.dev/posts/secure-aws-deploys-from-github-actions-with-oidc/#:~:text=Add%20GitHub%20as%20an%20identity%20provider&text=To%20do%20that%2C%20navigate%20to,Provider%20type%2C%20select%20OpenID%20Connect).
2. Create a new `CoffeebeansCoreGithubActions` Iam role with the following inline IAM policy

```json
   {
   "Version": "2012-10-17",
   "Statement": [
      {
         "Action": "iam:PassRole",
         "Resource": [
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-lookup-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-file-publishing-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-image-publishing-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-cfn-exec-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-deploy-role-{aws-account-number}-{aws-region}"
         ],
         "Effect": "Allow"
      }
   ]
}
```

and the following trust relationship

```json
{
   "Version": "2012-10-17",
   "Statement": [
      {
         "Effect": "Allow",
         "Principal": {
            "Federated": "arn:aws:iam::{aws-account-number}:oidc-provider/token.actions.githubusercontent.com"
         },
         "Action": "sts:AssumeRoleWithWebIdentity",
         "Condition": {
            "StringEquals": {
               "token.actions.githubusercontent.com:aud": "sts.amazonaws.com"
            },
            "StringLike": {
               "token.actions.githubusercontent.com:sub": "repo:{github-account-or-org}/spring-native-aws-lambda:*"
            }
         }
      }
   ]
}
```

3. Create a new `CDKBootstrapForCoffeebeansCore` IAM role for CDK bootstrap with the following IAM
   managed policy `CoffeebeansCoreCdkBootstrapAccess`

```json
{
   "Version": "2012-10-17",
   "Statement": [
      {
         "Sid": "ECRPermissions",
         "Effect": "Allow",
         "Action": [
            "ecr:CreateRepository",
            "ecr:DeleteRepository",
            "ecr:SetRepositoryPolicy",
            "ecr:DescribeRepositories"
         ],
         "Resource": "arn:aws:ecr:{aws-region}:{aws-account-number}:repository/cdk-{qualifier}-container-assets-{aws-account-number}-{aws-region}"
      },
      {
         "Sid": "IAMPermissions",
         "Effect": "Allow",
         "Action": [
            "iam:GetRole",
            "iam:CreateRole",
            "iam:DeleteRole",
            "iam:AttachRolePolicy",
            "iam:PutRolePolicy",
            "iam:DetachRolePolicy",
            "iam:DeleteRolePolicy"
         ],
         "Resource": [
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-lookup-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-file-publishing-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-image-publishing-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-cfn-exec-role-{aws-account-number}-{aws-region}",
            "arn:aws:iam::{aws-account-number}:role/cdk-{qualifier}-deploy-role-{aws-account-number}-{aws-region}"
         ]
      },
      {
         "Sid": "S3Permissions",
         "Effect": "Allow",
         "Action": [
            "s3:PutBucketPublicAccessBlock",
            "s3:CreateBucket",
            "s3:DeleteBucketPolicy",
            "s3:PutEncryptionConfiguration",
            "s3:GetEncryptionConfiguration",
            "s3:PutBucketPolicy",
            "s3:DeleteBucket",
            "s3:PutBucketVersioning"
         ],
         "Resource": [
            "arn:aws:s3:::{qualifier}-cdk-bucket"
         ]
      },
      {
         "Sid": "SSMPermissions",
         "Effect": "Allow",
         "Action": [
            "ssm:DeleteParameter",
            "ssm:AddTagsToResource",
            "ssm:GetParameters",
            "ssm:PutParameter"
         ],
         "Resource": "arn:aws:ssm:{aws-region}:{aws-account-number}:parameter/cdk-bootstrap/{qualifier}/version"
      }
   ]
}
```

4. Create an IAM managed policy `CoffeebeansCoreCdkExecutionAccess` to be used
   by `cdk-{qualifier}-cfn-exec-role-{aws-account-number}-{aws-region}` which is gonna be created by
   CDK

```json
{
   "Version": "2012-10-17",
   "Statement": [
      {
         "Sid": "S3Permissions",
         "Effect": "Allow",
         "Action": "s3:GetObject",
         "Resource": [
            "arn:aws:s3:::{qualifier}-cdk-bucket",
            "arn:aws:s3:::{qualifier}-cdk-bucket/*"
         ]
      },
      {
         "Sid": "AGWPermissions",
         "Effect": "Allow",
         "Action": [
            "apigateway:POST",
            "apigateway:DELETE",
            "apigateway:GET",
            "apigateway:PATCH",
            "apigateway:PUT"
         ],
         "Resource": [
            "arn:aws:apigateway:{aws-region}::/restapis",
            "arn:aws:apigateway:{aws-region}::/restapis/*",
            "arn:aws:apigateway:{aws-region}::/account"
         ]
      },
      {
         "Sid": "SNSPermissions",
         "Effect": "Allow",
         "Action": [
            "SNS:CreateTopic",
            "SNS:DeleteTopic",
            "SNS:Subscribe",
            "SNS:GetTopicAttributes",
            "SNS:ListSubscriptionsByTopic",
            "SNS:Unsubscribe",
            "SNS:TagResource",
            "SNS:UntagResource"
         ],
         "Resource": [
            "arn:aws:sns:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-dead-letter-topic"
         ]
      },
      {
         "Sid": "SQSPermissions",
         "Effect": "Allow",
         "Action": [
            "sqs:GetQueueAttributes",
            "sqs:CreateQueue",
            "sqs:DeleteQueue",
            "sqs:GetQueueUrl",
            "sqs:SetQueueAttributes",
            "sqs:ListQueues"
         ],
         "Resource": [
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-failure-queue-dlq",
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-success-queue-dlq",
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-failure-queue",
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-success-queue"
         ]
      },
      {
         "Sid": "LambdaPermissions",
         "Effect": "Allow",
         "Action": [
            "lambda:GetFunction",
            "lambda:ListFunctions",
            "lambda:DeleteFunction",
            "lambda:CreateFunction",
            "lambda:TagResource",
            "lambda:AddPermission",
            "lambda:RemovePermission",
            "lambda:PutFunctionEventInvokeConfig",
            "lambda:DeleteFunctionEventInvokeConfig",
            "lambda:UpdateFunctionCode",
            "lambda:ListTags",
            "lambda:UpdateFunctionConfiguration"
         ],
         "Resource": [
            "arn:aws:lambda:{aws-region}:{aws-account-number}:function:spring-native-aws-lambda-function",
            "arn:aws:lambda:{aws-region}:{aws-account-number}:function:spring-native-aws-lambda-function:$LATEST"
         ]
      },
      {
         "Sid": "SSMPermissions",
         "Effect": "Allow",
         "Action": [
            "ssm:GetParameters"
         ],
         "Resource": [
            "arn:aws:ssm:{aws-region}:{aws-account-number}:parameter/cdk-bootstrap/{qualifier}/version"
         ]
      },
      {
         "Sid": "IAMPermissions",
         "Effect": "Allow",
         "Action": [
            "iam:PassRole",
            "iam:GetRole",
            "iam:GetRolePolicy",
            "iam:CreateRole",
            "iam:PutRolePolicy",
            "iam:DeleteRole",
            "iam:DeleteRolePolicy",
            "iam:AttachRolePolicy",
            "iam:DetachRolePolicy"
         ],
         "Resource": [
            "arn:aws:iam::{aws-account-number}:role/spring-native-aws-lambda-springnativeawslambdafun-*",
            "arn:aws:iam::{aws-account-number}:role/spring-native-aws-lambda-springnativeawslambdares-4FVJBBHF9EL2",
            "arn:aws:iam::{aws-account-number}:role/spring-native-aws-lambda-function-rest-api/CloudWatchRole"
         ]
      },
      {
         "Sid": "CFNPermissions",
         "Effect": "Allow",
         "Action": "cloudformation:DescribeStacks",
         "Resource": "arn:aws:cloudformation:{aws-region}:{aws-account-number}:stack/{qualifier}-example-function-dev-stack/*"
      }
   ]
}
```

5. Run the following command to bootstrap the CDK

```bash
cdk bootstrap aws://{aws-account-number}/{aws-region} --profile cdk \
  --role-arn arn:aws:iam::{aws-account-number}:role/CDKBootstrapForCoffeebeansCore \
  --cloudformation-execution-policies "arn:aws:iam::{aws-account-number}:policy/CoffeebeansCoreCdkExecutionAccess" \
  --toolkit-stack-name cdk-{qualifier}-toolkit \
  --toolkit-bucket-name {qualifier}-cdk-bucket \
  --qualifier {qualifier} \
  --tags COST_CENTRE=coffeebeans-core
```

**NOTE**: notice that the policy passed to `--cloudformation-execution-policies` option is the one
we created
in step 4

**NOTE 2**: I added queues to receive messages from the functions `onSuccess` and `onFailure`
topics, so I had to add the SQS permissions to the policy. However, in production scenario I would
stop at the topics and let the processor applications create and subscribe the queues to the topics.
This way, the processor applications can be deployed independently from the function and the
function can be deployed independently from the processor applications. This is a good practice to
follow in order to have a loosely coupled architecture.

#### Building AWS Lambda Function from Zip

Now that the setup is done you can deploy to AWS.

1. Create a new release in
   Github [releases page](https://github.com/muhamadto/spring-native-aws-lambda/releases),
   the [github action](.github/workflows/release.yml) will start and a deployment to AWS
   environment.
2. Test via curl
    ```shell
   $ curl --location --request POST 'https://{api-id}.execute-api.ap-southeast-2.amazonaws.com/dev/name' \
   --header 'Content-Type: application/json' \
   --data-raw '{
        "name": "CoffeeBeans"
      }'
    ```
3. Et voila! It runs with 500 ms for cold start.

## Maven Repository

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
