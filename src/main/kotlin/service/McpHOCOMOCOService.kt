package org.example.service

import org.example.model.Motif
import java.io.File

class McpHOCOMOCOService {


    //qua famo er lavoro sporco di parsing file e tutto il resto poi nel client ce lo richiamiamo nei tool

    fun parseMemeFile(filePath: String): List<Motif> {
        val motifs = mutableListOf<Motif>()
        val lines = File(filePath).readLines()
        var currentMotif: Motif? = null
        var matrix = mutableListOf<List<Double>>()

        for (line in lines) {
            val trimmed = line.trim()

            // Ignora righe vuote o intestazioni MEME
            if (trimmed.isEmpty() || trimmed.startsWith("MEME") ||
                trimmed.startsWith("ALPHABET") ||
                trimmed.startsWith("strands") ||
                trimmed.startsWith("Background") ||
                trimmed.startsWith("URL")
            ) {
                continue
            }

            // Nuovo motif
            if (trimmed.startsWith("MOTIF")) {
                currentMotif?.let {
                    motifs.add(it.copy(matrix = matrix))
                }
                val motifName = trimmed.split(" ")[1]
                currentMotif = Motif(motifName, emptyList())
                matrix = mutableListOf()
                continue
            }

            // Riga intestazione matrix
            if (trimmed.startsWith("letter-probability matrix")) {
                continue
            }

            // Riga della matrice: prova a parsare i numeri, altrimenti ignora
            val probabilities = trimmed.split("\t", " ", "\n").mapNotNull {
                it.toDoubleOrNull()
            }

            if (probabilities.isNotEmpty()) {
                matrix.add(probabilities)
            }
        }

        // Aggiungi l'ultimo motif
        currentMotif?.let {
            motifs.add(it.copy(matrix = matrix))
        }

        return motifs
    }


    fun matchMotifToSequence(motif: Motif, sequence: String): List<Int> {
        val motifLength = motif.matrix.size
        val result = mutableListOf<Int>()

        for (i in 0..sequence.length - motifLength) {
            val subseq = sequence.substring(i, i + motifLength)
            val score = calculateScore(motif, subseq)
            if (score > 0.7) { // Puoi usare una soglia per il match
                result.add(i)  // Aggiungi la posizione di match
            }
        }
        return result
    }

    // Calcola la somiglianza tra la matrice del motivo e un segmento della sequenza
    fun calculateScore(motif: Motif, subseq: String): Double {
        var score = 0.0
        for (i in subseq.indices) {
            score += motif.matrix[i][nucleotideToIndex(subseq[i])]
        }
        return score
    }

    // Mappa i nucleotidi ACGT a indici nella matrice
    fun nucleotideToIndex(nucleotide: Char): Int = when (nucleotide) {
        'A' -> 0
        'C' -> 1
        'G' -> 2
        'T' -> 3
        else -> throw IllegalArgumentException("Invalid nucleotide")
    }



    fun scanMotifsInSequence(memeFilePath: String, sequence: String): Map<String, List<Int>> {
        val motifs = parseMemeFile(memeFilePath)
        val results = mutableMapOf<String, List<Int>>()

        for (motif in motifs) {
            val matches = matchMotifToSequence(motif, sequence)
            results[motif.name] = matches
        }

        return results
    }

}