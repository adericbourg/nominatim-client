package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.Place

data class SearchResponse(val places: List<Place>)
