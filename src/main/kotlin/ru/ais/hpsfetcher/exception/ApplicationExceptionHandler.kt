package ru.ais.hpsfetcher.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.ais.hpsfetcher.dto.common.ErrorResponse

@ControllerAdvice
class ApplicationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAllUncaughtException(e: Exception): ResponseEntity<Any> =
        with(HttpStatus.INTERNAL_SERVER_ERROR) {
            createErrorResponse(
                "Произошла непредвиденная ошибка",
                this.name, this
            )
        }.also {
            logger.error("Unexpected error occurred", e)
        }

//    @ExceptionHandler(RegCardNotFoundException::class)
//    fun handleRegCardException(
//        e: RegCardServiceBaseException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = REG_CARD_NOT_FOUND,
//        status = HttpStatus.valueOf(REG_CARD_NOT_FOUND_STATUS)
//    )
//
//    @ExceptionHandler(RegCardAlreadyExistsException::class)
//    fun handleRegCardAlreadyExists(
//        e: RegCardServiceBaseException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = REG_CARD_ALREADY_EXISTS,
//        status = HttpStatus.valueOf(REG_CARD_ALREADY_EXISTS_STATUS)
//    )
//
//    @ExceptionHandler(RegCardIdIsIncorrectException::class)
//    fun handleRegCardIncorrectId(
//        e: RegCardServiceBaseException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = REG_CARD_INCORRECT_ID,
//        status = HttpStatus.valueOf(REG_CARD_INCORRECT_ID_STATUS)
//    )
//
//    @ExceptionHandler(ObjectTypeNotSpecifiedException::class)
//    fun handleObjectTypeNotSpecified(
//        e: ObjectTypeNotSpecifiedException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = OBJECT_TYPE_NOT_SPECIFIED,
//        status = HttpStatus.valueOf(OBJECT_TYPE_NOT_SPECIFIED_STATUS)
//    )
//
//    @ExceptionHandler(FileNotFoundException::class)
//    fun handleFileNotFound(
//        e: FileNotFoundException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = FILE_NOT_FOUND,
//        status = HttpStatus.valueOf(FILE_NOT_FOUND_STATUS)
//    )
//
//    @ExceptionHandler(ObjectNotFoundException::class)
//    fun handleObjectNotFound(
//        e: ObjectNotFoundException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = OBJECT_NOT_FOUND,
//        status = HttpStatus.valueOf(OBJECT_NOT_FOUND_STATUS)
//    )
//
//    @ExceptionHandler(ApplicantNotFoundException::class)
//    fun handleApplicantNotFound(
//        e: ApplicantNotFoundException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = APPLICANT_NOT_FOUND,
//        status = HttpStatus.valueOf(APPLICANT_NOT_FOUND_STATUS)
//    )
//
//    override fun handleHttpMessageNotReadable(
//        ex: HttpMessageNotReadableException, headers: HttpHeaders,
//        status: HttpStatus, request: WebRequest
//    ): ResponseEntity<Any> = createErrorResponse(
//        "Incorrect request body" + ex.detailMessage().let { if (it.isNotEmpty()) ": $it" else "" },
//        status.name, status
//    )
//
//    override fun handleExceptionInternal(
//        ex: Exception, body: Any?, headers: HttpHeaders,
//        status: HttpStatus, request: WebRequest
//    ): ResponseEntity<Any> = createErrorResponse(
//        ex.message ?: "", status.name, status
//    )
//
    private fun createErrorResponse(
        message: String, error: String,
        status: HttpStatus = HttpStatus.BAD_REQUEST,
    ): ResponseEntity<Any> = ResponseEntity.status(status)
        .body(
            ErrorResponse(
                status.value(), message, error)
        )
//
//    @ExceptionHandler(PropertyValueException::class)
//    fun handlePropertyValueExceptionException(
//        e: PropertyValueException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = "Поле ${e.propertyName} не передано в запросе для объекта ${e.entityName}",
//        error = FIELD_NOT_FOUND,
//        status = HttpStatus.valueOf(FIELD_NOT_FOUND_STATUS)
//    )
//
//    @ExceptionHandler(javax.validation.ConstraintViolationException::class)
//    fun handlePropertyValidationValueExceptionException(
//        e: javax.validation.ConstraintViolationException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = "Поля ${e.constraintViolations.joinToString { it.propertyPath.toString() }} не переданы",
//        error = FIELD_NOT_FOUND,
//        status = HttpStatus.valueOf(FIELD_NOT_FOUND_STATUS)
//    )
//
//    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException::class)
//    fun handlePropertyValidationValueExceptionException(
//        e: org.hibernate.exception.ConstraintViolationException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = "Поле \"${e.constraintName}\" не передано",
//        error = FIELD_NOT_FOUND,
//        status = HttpStatus.valueOf(FIELD_NOT_FOUND_STATUS)
//    )
//
//    override fun handleMethodArgumentNotValid(
//        ex: MethodArgumentNotValidException,
//        headers: HttpHeaders,
//        status: HttpStatus,
//        request: WebRequest
//    ): ResponseEntity<Any> {
//        if (ex.bindingResult.hasErrors() && ex.bindingResult.allErrors[0] is FieldError) {
//            (ex.bindingResult.allErrors[0] as FieldError).let {
//                return createErrorResponse(
//                    message = "Поле \"${it.field}\" содержит некорректное" +
//                            " значение \"${it.rejectedValue}\"" +
//                            (if (it.defaultMessage?.isNotEmpty() == true) ": ${it.defaultMessage}" else ""),
//                    error = FIELD_NOT_VALID,
//                    status = HttpStatus.valueOf(FIELD_NOT_VALID_STATUS)
//                )
//            }
//        }
//        return super.handleMethodArgumentNotValid(ex, headers, status, request)
//    }
//
//    @ExceptionHandler(SpaceInputException::class)
//    fun handleSpaceInput(
//        e: SpaceInputException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = SPACE_INPUT
//    )
//
//    @ExceptionHandler(UpdateInExternalSystemFailedException::class)
//    fun handleUpdateInExternalSystemFailed(
//        e: UpdateInExternalSystemFailedException
//    ): ResponseEntity<Any> = createErrorResponse(
//        message = e.message, error = UPDATE_IN_EXTERNAL_SYSTEM_FAILED
//    )
}
//
//fun NestedRuntimeException.detailMessage(): String = this.message.orEmpty().substringBefore("; nested exception is")