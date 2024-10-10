
# Flow Log Analyzer

## Overview
The Flow Log Analyzer is a Java program designed to parse flow log data and categorize each entry based on a predefined lookup table. It generates counts of tagged entries and unique port/protocol combinations, providing valuable insights into network traffic.

## Features
- Parses flow logs in the default AWS format (version 2).
- Matches entries to tags based on destination port and protocol.
- Generates two output files:
  - **Tag Counts:** Summary of how many times each tag was matched.
  - **Port/Protocol Counts:** Summary of unique port/protocol combinations.

## Input Files
- **Flow Logs:** A text file containing flow log entries. Example filename: `flow_logs.txt`.
- **Lookup Table:** A CSV file mapping destination ports and protocols to tags. Example filename: `lookup_table.csv`.

## Sample Files
- `flow_logs.txt`: Contains sample flow log data.
- `lookup_table.csv`: Contains mappings of destination ports and protocols to tags.

## Output Files
- `tag_counts.csv`: Lists each tag and the corresponding count of matches.
- `port_protocol_counts.csv`: Lists each unique port/protocol combination and its count.

## How to Run
1. Ensure you have Java installed on your machine.
2. Clone this repository or download the source code.
3. Place your input files (`flow_logs.txt` and `lookup_table.csv`) in the same directory as the Java file.
4. Compile the program:
   ```bash
   javac FlowLogAnalyzer.java
5. Run the program:
   ```bash
   java FlowLogAnalyzer
   

