package org.example.server

import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.*
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

    // Definition of schema input
    val getMotifsInput = Tool.Input(
        buildJsonObject {
            put("type", "object")
            put("properties", buildJsonObject {
                put("filePath", buildJsonObject {
                    put("type", "string")
                    put("description", "Path to the MEME file which contains the motifs")
                })
                put("sequence", buildJsonObject {
                    put("type", "string")
                    put("description", "Target genomic sequence (ACGT)")
                })
            })
            put("required", buildJsonArray {
                add("filePath")
                add("sequence")
            })
        }
    )

    // Tool registration
    server.addTool(
        "getMotifs",
        "Make a scan of the sequence with the motifs defined in a MEME file",
        getMotifsInput
    ) { input ->
        val filePath = input.arguments["filePath"]!!.jsonPrimitive.content
        val sequence = input.arguments["sequence"]!!.jsonPrimitive.content

        // Call the service through the client
        val results = client.service.scanMotifsInSequence(filePath, sequence)

        CallToolResult(
            listOf(
                TextContent("Results: $results")
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
