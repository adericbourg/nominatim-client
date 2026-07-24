package net.dericbourg.nominatim.client

abstract class NominatimException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
class BadRequestException(message: String) : NominatimException(message)
class UnavailableException(message: String) : NominatimException(message)
