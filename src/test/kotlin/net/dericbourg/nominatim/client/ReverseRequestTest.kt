package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.OsmType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ReverseRequestTest {

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
    fun `reverse by coordinates sends lat and lon`() {
        server.enqueue(MockResponse().setBody(SAMPLE_BODY))

        client.reverse(ReverseRequest.coordinates(lat = 48.8582876, lon = 2.2944758))

        val url = server.takeRequest().requestUrl
        assertEquals("48.8582876", url?.queryParameter("lat"))
        assertEquals("2.2944758", url?.queryParameter("lon"))
        assertNull(url?.queryParameter("osm_type"))
    }

    @Test
    fun `reverse by osm object sends osm_type and osm_id`() {
        server.enqueue(MockResponse().setBody(SAMPLE_BODY))

        client.reverse(ReverseRequest.osmObject(OsmType.NODE, 7992604958))

        val url = server.takeRequest().requestUrl
        assertEquals("N", url?.queryParameter("osm_type"))
        assertEquals("7992604958", url?.queryParameter("osm_id"))
        assertNull(url?.queryParameter("lat"))
    }

    @Test
    fun `reverse deserializes a successful result`() {
        server.enqueue(MockResponse().setBody(SAMPLE_BODY))

        val result = client.reverse(ReverseRequest.coordinates(lat = 48.8582876, lon = 2.2944758))

        assertNotNull(result)
        assertEquals(98410321L, result.placeId)
        assertEquals("node", result.osmType)
        assertEquals(7992604958L, result.osmId)
        assertEquals("France", result.address.country)
    }

    @Test
    fun `reverse returns null when Nominatim reports no match`() {
        server.enqueue(MockResponse().setBody("""{"error":"Unable to geocode"}"""))

        val result = client.reverse(ReverseRequest.coordinates(lat = 0.0, lon = 0.0))

        assertNull(result)
    }

    companion object {
        // Captured from a real https://nominatim.openstreetmap.org/reverse response.
        private val SAMPLE_BODY = """
            {
                "place_id": 98410321,
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
        """.trimIndent()
    }
}
