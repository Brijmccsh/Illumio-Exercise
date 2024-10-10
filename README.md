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
    ```bash
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

## Testing and Analysis

### Tests Performed

1. Basic Functionality Test:
   - Created a sample flow log file and a basic lookup table to verify core functionality.
   - Checked if the program correctly parsed log entries and matched tags.
   - Verified the accuracy of tag counts and port/protocol counts in output files.

2. Diverse Log Entries Test:
   - Generated a more comprehensive set of log entries covering various scenarios:
     - Different protocols (TCP, UDP)
     - Well-known ports (80, 443, 53) and high-numbered ephemeral ports
     - Both ACCEPT and REJECT actions
     - Various source and destination IP ranges (private and public)
   - Ensured the program correctly handled and categorized this diverse set of logs.

3. Edge Case Testing:
   - Tested with an empty flow log file to verify proper handling of no input.
   - Included malformed log entries to check error handling and robustness.

4. Lookup Table Variations:
   - Tested with an empty lookup table to ensure proper handling of untagged entries.
   - Included case variations in the lookup table to verify case-insensitive matching.

5. Output Verification:
   - Verified the format and content of both tag count and port/protocol count output files.

### Code Analysis

1. Modularity:
   - The program is structured with separate methods for loading the lookup table, processing logs, and writing output, enhancing readability and maintainability.

2. Efficiency:
   - Uses HashMaps for the lookup table and counters, providing O(1) average time complexity for lookups and updates.
   - Processes flow logs line by line, keeping memory usage constant regardless of file size.

3. Robustness:
   - Implements basic error handling to manage IO exceptions, enhancing reliability.
   - Successfully handles various input scenarios, including edge cases and diverse log entries.

4. Limitations:
   - Currently supports only the default AWS VPC Flow Log format (version 2).
   - Uses hardcoded file names, which may limit flexibility in different environments.

5. Scalability:
   - The current implementation should handle files up to 10 MB efficiently.
   - For larger datasets, consider implementing batch processing or using a database for the lookup table.

6. Testing Approach:
   - The integration testing approach verifies the overall functionality but doesn't test individual components in isolation.
   - This method is effective for ensuring end-to-end correctness but may make it harder to pinpoint issues in specific components.

### Areas for Improvement

1. Unit tests for individual methods to complement the integration tests.
2. More robust error handling and logging for better debugging and error tracking.
3. Introduce configuration options or command-line arguments for file paths to increase flexibility.
4. Supporting compressed log files to handle larger datasets more efficiently.
5. Multi-threading for processing large log files to improve performance on multi-core systems.

These tests and analyses ensure that the FlowLogAnalyzer functions correctly across various scenarios and provides insights into its performance characteristics. The identified areas for improvement offer a roadmap for future enhancements to the program.