//package ru.alfains.integration.sed.regcard.http
//
//
//open class HttpResponseException(
//    override val message: String, cause: Throwable? = null
//) : RuntimeException(message, cause)
//
//class IncorrectResponseBodyException(cause: Throwable? = null) :
//    HttpResponseException("Unable to parse json from response", cause)
//
//open class HttpResponseErrorException(
//    override val message: String
//) : HttpResponseException(message)
