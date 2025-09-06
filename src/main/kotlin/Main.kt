package org.example

import org.example.service.McpHOCOMOCOService

fun main() {
    val service = McpHOCOMOCOService()

    val filePath = "C:\\Users\\Utente\\Desktop\\McpHOCOMOCO_2025\\HOCOMOCOv11_core_HUMAN_mono_meme_format.meme" // metti il percorso corretto del file MEME
    val sequence = "ATCGATCGATCGATCG" // una sequenza di test

    try {
        val results = service.scanMotifsInSequence(filePath, sequence)
        println("Risultati scansione: $results")
    } catch (e: Exception) {
        println("Errore durante la scansione dei motifs:")
        e.printStackTrace()
    }
}
