package com.triple.homework.common.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EnumValidator(

    private var annotation: EnumValid?,
): ConstraintValidator<EnumValid, String> {

    override fun initialize(constraintAnnotation: EnumValid?) {
        annotation = constraintAnnotation
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        // TODO: enum을 가져와서 검증해야한다.
        return false
    }
}