package com.coffeebeans.springnativeawslambda.infra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String KEY_ENV = "ENVIRONMENT";
    public static final String KEY_COST_CENTRE = "COST_CENTRE";
    public static final String KEY_APPLICATION_NAME = "APPLICATION_NAME";
    public static final String KEY_APPLICATION_VALUE = "spring-native-aws-function";
}
