package net.dericbourg.nominatim.api

/** An OpenStreetMap element type, as used in Nominatim's `osm_type`/`osm_id` parameters. */
enum class OsmType(internal val code: String) {
    NODE("N"),
    WAY("W"),
    RELATION("R"),
}
