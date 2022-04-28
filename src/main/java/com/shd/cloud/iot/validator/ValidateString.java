package com.shd.cloud.iot.validator;

import javax.validation.Payload;

public @interface ValidateString {
    String[] acceptedValues();

    String message() default "must match \"{acceptedValues}\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
