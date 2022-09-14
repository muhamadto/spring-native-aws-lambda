# spring-native-aws-lambda

[![CodeQL](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/codeql-analysis.yml)
[![Build](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/build.yml/badge.svg)](https://github.com/muhamadto/spring-native-aws-lambda/actions/workflows/build.yml)
[![jdk](./badges/jdk.svg)](https://jdk.java.net/17/)
[![graalvm](./badges/graalvm.svg)](https://www.graalvm.org/release-notes/22_2/)
[![spring-native](./badges/spring-native.svg)](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
[![spring-cloud](./badges/spring-cloud.svg)](https://spring.io/projects/spring-cloud)
[![spring-boot](./badges/spring-boot.svg)](https://spring.io/projects/spring-boot)
[![aws-cdk](./badges/aws-cdk.svg)](https://docs.aws.amazon.com/cdk/v2/guide/home.html)

## Test

```bash
$ sdk use java 22.2.r17-grl
$ ./mvnw -ntp clean verify -U --settings ./settings-spring.xml
```

## Building and Running

### Locally

1. Run the following commands
    ```shell
    $ ./mvnw -ntp clean package -U -Pnative --settings ./settings-spring.xml
    $ ./spring-native-aws-lambda-function/target/spring-native-aws-lambda-function
    ```
   The service starts in less than 100 ms
   ```shell
   2022-12-06 23:54:56.539  INFO 19823 --- [           main] o.s.nativex.NativeListener               : AOT mode enabled
   2022-12-06 23:54:56.541  INFO 19823 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : Starting SpringNativeAwsLambdaApplication using Java 17.0.4
   2022-12-06 23:54:56.542  INFO 19823 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : No active profile set, falling back to 1 default profile: "default"
   2022-12-06 23:54:56.562  INFO 19823 --- [           main] c.c.s.SpringNativeAwsLambdaApplication   : Started SpringNativeAwsLambdaApplication in 0.068 seconds (JVM running for 0.071)
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
            "arn:aws:s3:::{qualifier}-cdk-bucket/",
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
            "SNS:GetTopicAttributes",
            "SNS:CreateTopic",
            "SNS:DeleteTopic",
            "SNS:Subscribe"
         ],
         "Resource": [
            "arn:aws:sns:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-success-topic.fifo",
            "arn:aws:sns:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-failure-topic"
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
            "sqs:SetQueueAttributes"
         ],
         "Resource": [
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-failure-queue-dlq",
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-success-queue-dlq.fifo",
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-failure-queue",
            "arn:aws:sqs:{aws-region}:{aws-account-number}:spring-native-aws-lambda-function-success-queue.fifo"
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
            "lambda:DeleteFunctionEventInvokeConfig"
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

**NOTE**: notice that the policy passed in `--cloudformation-execution-policies` is the one created
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
