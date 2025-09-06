             +----------------+
             |   LLM / Agent  |
             | (Claude, IDE…) |
             +-------+--------+
                     |
                     |  JSON-RPC 2.0 over HTTP or stdio
                     v
              +------+------+
              |  MCP Client |  ← integrato nell'host (Claude, plugin, ecc.)
              +------+------+
                     |
                     |  comunica con...
                     v
              +-------------+
              | MCP Server  |  ← il tuo SDK/tool "gira" qui
              +------+------+
                     |
                     |  Tools / Resources / Prompts
                     v
            +--------------------+
            | Il tuo MCP Tool    |
            | (es: callTool)     |
            +--------------------+


🎯 Esempi pratici
▶ Scenario 1 – Locale

Hai un tool chiamato MyMCPTool che interagisce con il filesystem.

Scrivi un MCP Server in Kotlin (con JSON-RPC su stdio)

L’host (Claude Desktop, IDE, ecc.) lo esegue come processo locale

L’host invia callTool("myTool", args) e riceve output

▶ Scenario 2 – Remoto

Hai un tool che fa scraping web o interagisce con un DB aziendale.

Fai girare il server MCP su una VPS / cloud

Espone una JSON-RPC endpoint HTTP /mcp

L’agente/host lo contatta via callTool("myTool") attraverso la rete


| Aspetto                | Locale                                                               | Remoto                                                                              |
| ---------------------- | -------------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| ⚙️ Setup               | Gira sul PC dello user (es. un ricercatore con IDE o Claude Desktop) | Gira su un server accessibile da più utenti                                         |
| 📁 Accesso a file MEME | Facile se l’utente ha file locali                                    | Serve upload, o storage condiviso                                                   |
| 🚀 Velocità iniziale   | Immediata, senza deploy web                                          | Va configurato localmente                                                           |
| 🌍 Condivisione        | Difficile (ogni utente deve installarlo)                             | Molto più semplice (1 endpoint centrale)                                            |
| 🛡 Sicurezza           | Nessun rischio di accesso a file sensibili online                    | Serve gestire ACL, sicurezza API, ecc.                                              |
| ☁️ Esempio di uso      | Ricercatore usa Claude o ChatGPT su file MEME locale                 | Claude, o altro LLM, si connette al tuo MCP Server bio su cloud e invia la sequenza |