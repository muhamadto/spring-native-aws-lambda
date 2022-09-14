package com.coffeebeans.springnativeawslambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.ResourceHint;

@NativeHint(
    resources = {@ResourceHint(patterns = {
        "org.joda.time.tz.*"
    })})
@SpringBootApplication
public class SpringNativeAwsLambdaApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringNativeAwsLambdaApplication.class, args);
  }
}
