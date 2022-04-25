package com.xquare.v1userservice.configuration.validate

import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class RequestBodyValidator(
    private val validator: Validator
) {

    fun validate(target: Any) {
        val errors = BeanPropertyBindingResult(target, "target")

        validator.validate(target, errors)

        if (errors.hasErrors()) {
            onHasError(errors)
        }
    }

    private fun onHasError(errors: Errors) {
        val errorMessage = errors.fieldErrors
            .joinToString { "${it.field}은(는) ${it.defaultMessage}" }
        throw BadRequestException(errorMessage)
    }

}
