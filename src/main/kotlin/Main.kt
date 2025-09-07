package org.example

import org.example.service.McpHOCOMOCOService

fun main() {
    val service = McpHOCOMOCOService()

    val filePath = "{ABSOLUTE_PATH}\\McpHOCOMOCO_2025\\HOCOMOCOv11_core_HUMAN_mono_meme_format.meme" //substitute with your absolute path
    val sequence = "ATCGATCGATCGATCG" // test sequence

    try {
        val results = service.scanMotifsInSequence(filePath, sequence)
        println("Results: $results")
    } catch (e: Exception) {
        println("Error during scanMotifsInSequence:") //I will add logs later
        e.printStackTrace()
    }
}
