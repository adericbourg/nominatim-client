package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.OsmType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LookupRequestTest {

    private lateinit var server: MockWebServer
    private lateinit var client: NominatimClient

    @BeforeEach
    fun startServer() {
        server = MockWebServer()
        server.start()
        client = NominatimHttpClient("acceptance-test-agent/1.0", server.url("/").toString())
    }

    @AfterEach
    fun stopServer() {
        server.shutdown()
    }

    @Test
    fun `lookup sends osm_ids prefixed with their type`() {
        server.enqueue(MockResponse().setBody("[]"))

        client.lookup(
            LookupRequest(osmIds = listOf(OsmId(OsmType.WAY, 5013364), OsmId(OsmType.NODE, 7992604958)))
        )

        val url = server.takeRequest().requestUrl
        assertEquals("W5013364,N7992604958", url?.queryParameter("osm_ids"))
    }

    @Test
    fun `lookup returns an empty list for unmatched ids`() {
        server.enqueue(MockResponse().setBody("[]"))

        val result = client.lookup(LookupRequest(osmIds = listOf(OsmId(OsmType.WAY, 1))))

        assertEquals(emptyList(), result)
    }

    @Test
    fun `lookup deserializes matched places`() {
        server.enqueue(MockResponse().setBody(SAMPLE_BODY))

        val result = client.lookup(LookupRequest(osmIds = listOf(OsmId(OsmType.NODE, 7992604958))))

        assertEquals(1, result.size)
        val place = result[0]
        assertEquals(98875594L, place.placeId)
        assertEquals("node", place.osmType)
        assertEquals(7992604958L, place.osmId)
        assertEquals("France", place.address.country)
    }

    @Test
    fun `lookup omits optional parameters when unset`() {
        server.enqueue(MockResponse().setBody("[]"))

        client.lookup(LookupRequest(osmIds = listOf(OsmId(OsmType.NODE, 1))))

        val url = server.takeRequest().requestUrl
        assertNull(url?.queryParameter("accept-language"))
        assertNull(url?.queryParameter("email"))
        assertNull(url?.queryParameter("extratags"))
    }

    companion object {
        // Captured from a real https://nominatim.openstreetmap.org/lookup response.
        private val SAMPLE_BODY = """
            [
                {
                    "place_id": 98875594,
                    "licence": "Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright",
                    "osm_type": "node",
                    "osm_id": 7992604958,
                    "lat": "48.8582876",
                    "lon": "2.2944758",
                    "category": "emergency",
                    "type": "defibrillator",
                    "place_rank": 30,
                    "importance": 9.317235065227768e-05,
                    "addresstype": "emergency",
                    "name": "",
                    "display_name": "5, Avenue Anatole France, Paris, France",
                    "address": {
                        "house_number": "5",
                        "road": "Avenue Anatole France",
                        "city": "Paris",
                        "county": "Paris",
                        "state": "Île-de-France",
                        "postcode": "75007",
                        "country": "France",
                        "country_code": "fr"
                    },
                    "namedetails": null,
                    "boundingbox": ["48.8582376", "48.8583376", "2.2944258", "2.2945258"]
                }
            ]
        """.trimIndent()
    }
}
