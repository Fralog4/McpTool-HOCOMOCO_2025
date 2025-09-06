package org.example.server

import io.ktor.client.request.invoke
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.example.client.McpHOCOMOCOClientImpl
import org.example.service.McpHOCOMOCOService

fun createServer(): Server {
    val info = Implementation(
        "MCP HOCOMOCO",
        "1.0.0"
    )

    val options = ServerOptions(
        capabilities = ServerCapabilities(tools = ServerCapabilities.Tools(true))
    )

    val server = Server(
        info,
        options
    )

    val client = McpHOCOMOCOClientImpl(
        McpHOCOMOCOService()
    )

    // Definizione dello schema di input del tool
    val getMotifsInput = Tool.Input(
        buildJsonObject {
            put("type", "object")
            put("properties", buildJsonObject {
                put("filePath", buildJsonObject {
                    put("type", "string")
                    put("description", "Path al file MEME contenente i motifs")
                })
                put("sequence", buildJsonObject {
                    put("type", "string")
                    put("description", "Sequenza genomica target (ACGT)")
                })
            })
            put("required", buildJsonArray {
                add("filePath")
                add("sequence")
            })
        }
    )

    // Registrazione del tool
    server.addTool(
        "getMotifs",
        "Scansiona una sequenza con i motifs definiti in un file MEME",
        getMotifsInput
    ) { input ->
        val filePath = input.arguments["filePath"]!!.jsonPrimitive.content
        val sequence = input.arguments["sequence"]!!.jsonPrimitive.content

        // Chiamo il service attraverso il client
        val results = client.service.scanMotifsInSequence(filePath, sequence)

        CallToolResult(
            listOf(
                TextContent("Risultati scansione: $results")
            )
        )
    }

    return server
}

fun main() {
    val server = createServer()
    val stdioServerTransport = StdioServerTransport(
        System.`in`.asSource().buffered(),
        System.`out`.asSink().buffered()
    )
    runBlocking {
        val job = Job()
        server.onCloseCallback = { job.complete() }
        server.connect(stdioServerTransport)
        job.join()
    }
}
