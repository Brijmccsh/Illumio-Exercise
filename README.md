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
1. We need to have Java installed on the machine.
2. Clone this repository or download the source code.
3. Place your input files `flow_logs.txt` (our flow log data), `lookup_table.csv` (our lookup table) files in the same directory as the Java file.
4. Compile the program:
   ```bash
   javac FlowLogAnalyzer.java
5. Run the program:
   ```bash
   java FlowLogAnalyzer

## Assumptions and Limitations

1. Log Format: The program only supports the default AWS VPC Flow Log format (version 2). Custom log formats are not supported.

2. Log Version: Only version 2 of AWS VPC Flow Logs is supported.

3. Lookup Table: The lookup table is expected to be a CSV file with three columns: dstport, protocol, and tag.

4. Case Sensitivity: Matching for ports and protocols is case-insensitive.

5. File Size: The program is designed to handle flow log files up to 10 MB in size.

6. Lookup Table Size: The lookup table can contain up to 10,000 mappings.

7. Multiple Mappings: Tags can map to more than one port/protocol combination.

8. File Encoding: Input files (flow logs and lookup table) are assumed to be plain text (ASCII) files.

9. File Names: The program expects specific file names:
   - Flow log file: `flow_logs.txt`
   - Lookup table file: `lookup_table.csv`

10. Output Files: The program generates two output files:
    - `tag_counts.csv`: Contains counts of matches for each tag
    - `port_protocol_counts.csv`: Contains counts of matches for each port/protocol combination

11. Error Handling: Basic error handling is implemented. Errors are printed to the console.

12. Memory Usage: The program loads the entire lookup table into memory but processes the flow log file line by line to conserve memory.

## Prerequisites

- Java Development Kit (JDK) 8 or higher

## Requirements Evaluation

### Input files are plain text (ASCII) files
The solution uses `BufferedReader` to read both the flow log and lookup table files, ensuring compatibility with plain text files.

### The flow log file size can be up to 10 MB
The program processes the file line by line, ensuring memory efficiency. For larger files, consider adding progress reporting or chunked processing for better scalability.
    ```java
    BufferedReader reader = new BufferedReader(new FileReader("flow_logs.txt"));
    String line;
    while ((line = reader.readLine()) != null) {
        // Process each line
    }   
### The lookup file can have up to 10,000 mappings
The solution leverages a HashMap to store mappings, which efficiently handles large datasets.    

### Tags can map to more than one port/protocol combination
The structure allows for multiple mappings to a single tag. For example, both ports 25 and 23 can map to the tag sv_P1.

### Matches should be case insensitive
The lookup key is generated using toLowerCase(), ensuring case-insensitive matching.