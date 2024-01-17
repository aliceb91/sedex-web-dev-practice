package hello

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.and
import org.http4k.core.Headers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.http4k.format.Jackson.asJsonValue
import org.http4k.format.Jackson.asJsonObject
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasHeader
import org.http4k.hamkrest.hasStatus

class HelloClientTest {

    val server = helloServer()
    @BeforeEach
    fun setup() {
        server.start()
    }

    @AfterEach
    fun teardown() {
        server.stop()
    }

    @Test
    fun `it returns Hello when hello is called with no parameters`() {
        val underTest = HelloClient("http://localhost:9000")
        val expected: String = "Hello"
        val result: String = underTest.hello()
        assertEquals(expected, result)
    }

    @Test
    fun `it returns hello in the relevant langauge for the relevant argument`() {
        val underTest = HelloClient("http://localhost:9000")
        val expected1: String = "Bonjour"
        val expected2: String = "Salve"
        val result1: String = underTest.hello("fr-FR")
        val result2: String = underTest.hello("it-IT")
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
    }

    @Test
    fun `it returns hello with the given name when provided`() {
        val underTest = HelloClient("http://localhost:9000")
        val expected1: String = "Hello Alice"
        val expected2: String = "Hello Jess"
        val result1: String = underTest.hello(name = "Alice")
        val result2: String = underTest.hello(name = "Jess")
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
    }

    @Test
    fun `it returns the relevant language greeting when a name is given`() {
        val underTest = HelloClient("http://localhost:9000")
        val expected1: String = "G'day Alice"
        val expected2: String = "Alright, Alice?"
        val result1: String = underTest.hello(lang = "en-AU", name = "Alice")
        val result2: String = underTest.hello(lang = "en-GB", name = "Alice")
        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
    }

    @Test
    fun `it returns all headers when echoHeaders is called`() {
        val underTest = HelloClient("http://localhost:9000")
        val expected: Map<String, String> =mapOf(
            "Host" to "localhost:9000",
            "User-agent" to "Java-http-client/11.0.21",
            "Content-length" to "0"
        )
        val result: Map<String, String> = underTest.echoHeaders()
        assertEquals(expected, result)
    }

    @Test
    fun `it returns all headers as a json object when indicated`() {
        val underTest = HelloClient("http://localhost:9000")
        val expected: Map<String, String> = mapOf(
            "Host" to "localhost:9000",
            "User-agent" to "Java-http-client/11.0.21",
            "Content-type" to "application/json",
            "Content-length" to "0"
        )
        val result: Map<String, String> = underTest.echoHeaders(json = true)
        assertEquals(expected, result)
    }

    @Test
    fun `it returns all headers with a prefix as response headers when a prefix is given`() {
        val underTest = HelloClient("http://localhost:9000")
        val result: Map<String, String> = underTest.echoHeaders(prefix = "X-Echo-")
        assertEquals(true, result.containsKey("x-echo-content-length"))
        assertEquals(true, result.containsKey("x-echo-host"))
        assertEquals(true, result.containsKey("x-echo-user-agent"))
    }
}