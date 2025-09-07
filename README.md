             # McpHOCOMOCO - Transcription Factor Binding Site Scanner

A Kotlin-based tool for scanning DNA sequences to identify potential transcription factor binding sites using position weight matrices (PWMs) from the HOCOMOCO database in MEME format.

## Features

- Parse MEME format motif files (specifically HOCOMOCO database format)
- Scan DNA sequences for potential transcription factor binding sites
- Configurable matching threshold for motif detection
- Simple and efficient implementation in Kotlin

## Prerequisites

- Java 24 or higher (JVM 24)
- Gradle 7.6+

## Getting Started

### Building the Project

```bash
gradle build
```

### Creating a Fat JAR

To create an executable JAR with all dependencies:

```bash
gradle shadowJar
```

The output will be in `build/libs/mcp-hocomoco-all.jar`.

## Usage

1. Prepare your MEME format file containing the position weight matrices (PWMs).
2. Have your DNA sequence ready (ACGT characters only, case-insensitive).

### Running the Application

```bash
java -jar build/libs/mcp-hocomoco-all.jar <path_to_meme_file> <dna_sequence>
```

### Example

```kotlin

fun main() {
    val service = McpHOCOMOCOService()
    
    // Path to your MEME format file
    val filePath = "path/to/HOCOMOCOv11_core_HUMAN_mono_meme_format.meme"
    
    // Your DNA sequence to scan
    val sequence = "ATCGATCGATCGATCG"
    
    try {
        val results = service.scanMotifsInSequence(filePath, sequence)
        println("Found matches: $results")
    } catch (e: Exception) {
        println("Error during sequence scanning: ${e.message}")
    }
}
```

## Output Format

The `scanMotifsInSequence` function returns a `Map<String, List<Int>>` where:
- Key: Name of the motif
- Value: List of starting positions (0-based) in the sequence where the motif was found

## Customization

### Adjusting Match Threshold

You can modify the matching threshold in the `matchMotifToSequence` function of `McpHOCOMOCOService.kt`. The default threshold is set to 0.7.

```kotlin
if (score > 0.7) { // Adjust this value as needed
    result.add(i)
}
```

## File Formats

### MEME Format

The tool expects MEME format files containing position weight matrices. Example format:

```
MOTIF MOTIF_NAME
letter-probability matrix: ...
0.397 0.261 0.125 0.216
0.182 0.182 0.136 0.500
...
```

## Dependencies

- Kotlin Standard Library
- Ktor (for potential future HTTP server capabilities)
- Shadow Plugin (for creating fat JARs)

## How to use this tool inside Claude Desktop
- After you built the JAR you can use it inside Claude Desktop by adding it to the MCP servers in the claude_desktop_config.json file.
- You cna find the claude_desktop_config.json file in the claude_desktop_config.json file inside this project, just copy paste it and adjust it with your JAR path.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- HOCOMOCO database for providing transcription factor binding site models
- MEME Suite for the file format specification
## Help 
- Whoever want to help with this project, please open an issue or a pull request and feel free to contact me.
## MCP schema

             +----------------+
             |   LLM / Agent  |
             | (Claude, IDE…) |
             +-------+--------+
                     |
                     |  JSON-RPC 2.0 over HTTP or stdio
                     v
              +------+------+
              |  MCP Client |  ← inside the host (Claude, plugin, ecc.)
              +------+------+
                     |
                     |  communicate with...
                     v
              +-------------+
              | MCP Server  |  ← your tool is here inside the Server
              +------+------+
                     |
                     |  Tools / Resources / Prompts
                     v
            +--------------------+
            |     MCP Tool       |
            | (es: callTool)     |
            +--------------------+
