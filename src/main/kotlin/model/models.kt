package org.example.model

data class Motif(
    val name: String,
    val matrix: List<List<Double>>,
 //   val id: String,           // ID del fattore di trascrizione (da capire se serve)
   // var consensus: String,    // Sequenza di consenso
   // var width: Int,           // Larghezza del motivo
   // val instances: List<String> // Posizioni delle occorrenze nei dati
)

data class Context(
    val id: String,
    val sequence: String,       // Sequenza genomica
    val factors: List<String>,  // Fattori di trascrizione associati
    val state: String           // Stato dell'analisi (es. "Caricato", "Analizzando", "Completato") //enum meglio prossimamente
)
