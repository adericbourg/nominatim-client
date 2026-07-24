package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.Place

/** The result of a [SearchRequest], as returned by the Nominatim `/search` endpoint. */
data class SearchResponse(val places: List<Place>)
