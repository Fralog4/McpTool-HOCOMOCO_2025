package org.example.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.example.service.McpHOCOMOCOService
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class McpHOCOMOCOClientImpl(
    val service: McpHOCOMOCOService
) : McpHOCOMOCOClient {

    private val file = "" //prompt level for now
    private val sequenceTarget = "" //prompt level for now


    /**
     * We actually do not need all this stuff for local execution.
     * I'll leave it here for a day we could host this somewhere, idk.
     */

//    private val bridgeUrl = "https://192.168.178.162"
//
//    private val client = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json()
//        }
//        engine {
//            https {
//                trustManager = object : X509TrustManager {
//                    override fun checkClientTrusted(chain: Array<out X509Certificate?>?, authType: String?) {
//                    }
//
//                    override fun checkServerTrusted(chain: Array<out X509Certificate?>?, authType: String?) {}
//
//                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
//                }
//            }
//        }
//    }

    //Tool
//    override suspend fun getMotifs() {
//
//        service.scanMotifsInSequence(file, sequenceTarget)
//
//        val requestBody = buildJsonObject {
//            put("", "")
//            }
//
//        client.put(bridgeUrl){ //chiamata PUT
//            headers{
//                append("hue-application-key", "")
//            }
//            contentType(ContentType.Application.Json)
//            setBody(requestBody.toString())
//        }
//    }

    //Tool
    override suspend fun getMotifs() {
        val results = service.scanMotifsInSequence(file, sequenceTarget)
        println("Motifs: $results")
    }


    //Tool
    override suspend fun getMotifById() {
        TODO("Not yet implemented")
    }

    //Tool
    override suspend fun getMotifBySequence() {
        TODO("Not yet implemented")
    }

}