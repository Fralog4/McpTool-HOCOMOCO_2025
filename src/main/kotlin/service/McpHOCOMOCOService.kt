package org.example.service

import org.example.model.Motif
import java.io.File

class McpHOCOMOCOService {

    fun parseMemeFile(filePath: String): List<Motif> {
        val motifs = mutableListOf<Motif>()
        val lines = File(filePath).readLines()
        var currentMotif: Motif? = null
        var matrix = mutableListOf<List<Double>>()

        for (line in lines) {
            val trimmed = line.trim()

            // Ignore empty lines or MEME header lines
            if (trimmed.isEmpty() || trimmed.startsWith("MEME") ||
                trimmed.startsWith("ALPHABET") ||
                trimmed.startsWith("strands") ||
                trimmed.startsWith("Background") ||
                trimmed.startsWith("URL")
            ) {
                continue
            }

            // New motif
            if (trimmed.startsWith("MOTIF")) {
                currentMotif?.let {
                    motifs.add(it.copy(matrix = matrix))
                }
                val motifName = trimmed.split(" ")[1]
                currentMotif = Motif(motifName, emptyList())
                matrix = mutableListOf()
                continue
            }

            // Line of matrix header
            if (trimmed.startsWith("letter-probability matrix")) {
                continue
            }

            // Parse the matrix line: try to parse the numbers, otherwise ignore
            val probabilities = trimmed.split("\t", " ", "\n").mapNotNull {
                it.toDoubleOrNull()
            }

            if (probabilities.isNotEmpty()) {
                matrix.add(probabilities)
            }
        }

        // Add the last motif
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
            if (score > 0.7) { // threshold for matching next tip would be to make the user choose it
                result.add(i)  // Add the position of the match
            }
        }
        return result
    }

    //Calculate the familiarity between the matrix of the motif and the target sequence
    fun calculateScore(motif: Motif, subseq: String): Double {
        var score = 0.0
        for (i in subseq.indices) {
            score += motif.matrix[i][nucleotideToIndex(subseq[i])]
        }
        return score
    }

    //Map the nucleotides ACGT to indices in the matrix
    fun nucleotideToIndex(nucleotide: Char): Int = when (nucleotide) {
        'A' -> 0
        'C' -> 1
        'G' -> 2
        'T' -> 3
        else -> throw IllegalArgumentException("Invalid nucleotide")
    }


    //Scan the motifs in the sequence
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