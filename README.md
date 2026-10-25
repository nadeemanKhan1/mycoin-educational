# MyCoin – Educational Hybrid Proof-of-Work Blockchain

MyCoin is a fully educational Java blockchain project.

It demonstrates:
- Double SHA-256 Proof-of-Work
- A unique combinatorial “City Distance” puzzle (Yorkshire towns)
- UTXO transaction model
- Basic P2P block propagation
- File-based persistence
- Simple wallet address generation

> **This project has no monetary value and is not a real cryptocurrency.**

### Yorkshire Cities
`bradford` · `mirfield` · `leeds` · `dewsbury` · `huddersfield` · `shipley`

### Quick Start

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.mycoin.MyCoinNodeLauncher"
