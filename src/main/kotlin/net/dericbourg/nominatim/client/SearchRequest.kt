package net.dericbourg.nominatim.client

sealed interface SearchRequest {
    companion object {
        fun query(value: String): SearchRequest {
            return QuerySearchRequest(value)
        }
    }
}

internal data class QuerySearchRequest(val value: String) : SearchRequest
