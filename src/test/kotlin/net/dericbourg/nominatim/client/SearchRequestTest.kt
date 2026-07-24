package net.dericbourg.nominatim.client

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SearchRequestTest {

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
    fun `free-form search sends q`() {
        server.enqueue(MockResponse().setBody("[]"))

        client.search(SearchRequest.query("tour eiffel, paris"))

        val request = server.takeRequest()
        assertEquals("tour eiffel, paris", request.requestUrl?.queryParameter("q"))
        assertNull(request.requestUrl?.queryParameter("street"))
    }

    @Test
    fun `structured search sends only the provided components`() {
        server.enqueue(MockResponse().setBody("[]"))

        client.search(SearchRequest.structured(street = "avenue Anatole France", city = "Paris"))

        val request = server.takeRequest()
        val url = request.requestUrl
        assertEquals("avenue Anatole France", url?.queryParameter("street"))
        assertEquals("Paris", url?.queryParameter("city"))
        assertNull(url?.queryParameter("q"))
        assertNull(url?.queryParameter("country"))
    }

    @Test
    fun `optional result parameters are sent only when set`() {
        server.enqueue(MockResponse().setBody("[]"))

        client.search(
            SearchRequest.query("tour eiffel").copy(
                limit = 5,
                countryCodes = listOf("fr", "be"),
                viewBox = ViewBox(x1 = 2.0, y1 = 49.0, x2 = 3.0, y2 = 48.0),
                bounded = true,
                excludePlaceIds = listOf(1L, 2L),
                dedupe = false,
                acceptLanguage = "fr",
                email = "test@example.com",
                extraTags = true,
            )
        )

        val url = server.takeRequest().requestUrl
        assertEquals("5", url?.queryParameter("limit"))
        assertEquals("fr,be", url?.queryParameter("countrycodes"))
        assertEquals("2.0,49.0,3.0,48.0", url?.queryParameter("viewbox"))
        assertEquals("1", url?.queryParameter("bounded"))
        assertEquals("1,2", url?.queryParameter("exclude_place_ids"))
        assertEquals("0", url?.queryParameter("dedupe"))
        assertEquals("fr", url?.queryParameter("accept-language"))
        assertEquals("test@example.com", url?.queryParameter("email"))
        assertEquals("1", url?.queryParameter("extratags"))
    }

    @Test
    fun `optional result parameters are omitted when unset`() {
        server.enqueue(MockResponse().setBody("[]"))

        client.search(SearchRequest.query("tour eiffel"))

        val url = server.takeRequest().requestUrl
        assertNull(url?.queryParameter("limit"))
        assertNull(url?.queryParameter("countrycodes"))
        assertNull(url?.queryParameter("viewbox"))
        assertNull(url?.queryParameter("bounded"))
        assertNull(url?.queryParameter("exclude_place_ids"))
        assertNull(url?.queryParameter("dedupe"))
        assertNull(url?.queryParameter("accept-language"))
        assertNull(url?.queryParameter("email"))
        assertNull(url?.queryParameter("extratags"))
    }
}
