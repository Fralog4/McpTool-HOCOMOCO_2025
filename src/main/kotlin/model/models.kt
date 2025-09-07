package org.example.model

/**
 * Motif of the transcription factor, very simplified for testing.
 * Further development is needed.
 */
data class Motif(
    val name: String, // Name of the motif
    val matrix: List<List<Double>>, // Matrix of the motif
 //   val id: String,           // ID of the transcription factor
   // var consensus: String,    // Consensus sequence
   // var width: Int,           // Width of the motif
   // val instances: List<String> // Positions of the motif
)

/**
 * Context of the analysis, not used yet
 */
data class Context(
    val id: String,
    val sequence: String,       // Genomic sequence
    val factors: List<String>,  // Transcription factors associated
    val state: String           // Stato dell'analisi (es. "Uploaded", "Analyzed", "Completed") //enum?
)
