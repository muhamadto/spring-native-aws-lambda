package com.coffeebeans.springnativeawslambda;

import com.coffeebeans.springnativeawslambda.model.Request;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@NativeHint(
    resources = {
        @ResourceHint(patterns = {"org.joda.time.tz.*"})
    }
)
// SerializationHint is required to ensure that the ObjectMapper can convert the body of the api proxy request into a Coffeebeans Request object.
@TypeHint(types = Request.class, access = {TypeAccess.DECLARED_CONSTRUCTORS, TypeAccess.DECLARED_METHODS})
@SpringBootApplication
public class SpringNativeAwsLambdaApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringNativeAwsLambdaApplication.class, args);
  }
}
