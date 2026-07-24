package net.dericbourg.nominatim.client

/** Base class for errors thrown by [NominatimClient] operations. */
abstract class NominatimException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/** Thrown when Nominatim rejects the request (HTTP 4xx). */
class BadRequestException(message: String) : NominatimException(message)

/** Thrown when Nominatim fails to fulfill an otherwise valid request (e.g. HTTP 5xx). */
class UnavailableException(message: String) : NominatimException(message)
