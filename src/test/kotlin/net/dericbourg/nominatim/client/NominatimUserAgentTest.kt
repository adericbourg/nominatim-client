package net.dericbourg.nominatim.client

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NominatimUserAgentTest {

    private lateinit var server: MockWebServer

    @BeforeEach
    fun startServer() {
        server = MockWebServer()
        server.start()
    }

    @AfterEach
    fun stopServer() {
        server.shutdown()
    }

    @Test
    fun `search sends the configured User-Agent header`() {
        server.enqueue(MockResponse().setBody("[]"))

        val userAgent = "acceptance-test-agent/1.0"
        val api = NominatimRetrofitApi.create(userAgent, server.url("/").toString())
        api.search(mapOf("q" to "tour eiffel")).execute()

        val request = server.takeRequest()
        assertEquals(userAgent, request.getHeader("User-Agent"))
    }
}
