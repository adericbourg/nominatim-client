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
}
