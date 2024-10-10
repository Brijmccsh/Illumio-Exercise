import java.io.*;
import java.util.*;

public class FlowLogAnalyzerTest {
    private static final String FLOW_LOG_FILE = "flow_logs.txt";
    private static final String LOOKUP_TABLE_FILE = "lookup_table.csv";
    private static final String TAG_COUNT_OUTPUT = "tag_counts.csv";
    private static final String PORT_PROTOCOL_COUNT_OUTPUT = "port_protocol_counts.csv";

    public void setUp() throws IOException {
        // Create lookup table
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOOKUP_TABLE_FILE))) {
            writer.println("dstport,protocol,tag");
            writer.println("80,tcp,http");
            writer.println("443,tcp,https");
            writer.println("53,udp,dns");
        }

        // Create flow logs
        try (PrintWriter writer = new PrintWriter(new FileWriter(FLOW_LOG_FILE))) {
            writer.println("2 123456789012 eni-1234567890 10.0.0.1 192.168.1.1 80 12345 6 100 5000 1620140761 1620140821 ACCEPT OK");
            writer.println("2 123456789012 eni-0987654321 10.0.0.2 192.168.1.2 443 54321 6 200 10000 1620140762 1620140822 ACCEPT OK");
            writer.println("2 123456789012 eni-1122334455 10.0.0.3 192.168.1.3 53 33333 17 50 2500 1620140763 1620140823 ACCEPT OK");
            writer.println("2 123456789012 eni-5566778899 10.0.0.4 192.168.1.4 22 44444 6 75 3750 1620140764 1620140824 ACCEPT OK");
        }

        // Run the analyzer
        FlowLogAnalyzer analyzer = new FlowLogAnalyzer();
        analyzer.run();
    }

    public void tearDown() {
        new File(FLOW_LOG_FILE).delete();
        new File(LOOKUP_TABLE_FILE).delete();
        new File(TAG_COUNT_OUTPUT).delete();
        new File(PORT_PROTOCOL_COUNT_OUTPUT).delete();
    }

    public void testTagCounts() throws IOException {
        Map<String, Integer> expectedTagCounts = new HashMap<>();
        expectedTagCounts.put("http", 1);
        expectedTagCounts.put("https", 1);
        expectedTagCounts.put("dns", 1);
        expectedTagCounts.put("Untagged", 1);

        Map<String, Integer> actualTagCounts = readTagCounts();
        assert expectedTagCounts.equals(actualTagCounts) : "Tag counts do not match expected values";
    }

    public void testPortProtocolCounts() throws IOException {
        Map<String, Integer> expectedPortProtocolCounts = new HashMap<>();
        expectedPortProtocolCounts.put("80,tcp", 1);
        expectedPortProtocolCounts.put("443,tcp", 1);
        expectedPortProtocolCounts.put("53,udp", 1);
        expectedPortProtocolCounts.put("22,tcp", 1);

        Map<String, Integer> actualPortProtocolCounts = readPortProtocolCounts();
        assert expectedPortProtocolCounts.equals(actualPortProtocolCounts) : "Port/Protocol counts do not match expected values";
    }

    private Map<String, Integer> readTagCounts() throws IOException {
        Map<String, Integer> tagCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TAG_COUNT_OUTPUT))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                tagCounts.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        return tagCounts;
    }

    private Map<String, Integer> readPortProtocolCounts() throws IOException {
        Map<String, Integer> portProtocolCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PORT_PROTOCOL_COUNT_OUTPUT))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                portProtocolCounts.put(parts[0] + "," + parts[1], Integer.parseInt(parts[2]));
            }
        }
        return portProtocolCounts;
    }

    public static void main(String[] args) {
        FlowLogAnalyzerTest test = new FlowLogAnalyzerTest();
        try {
            test.setUp();
            test.testTagCounts();
            test.testPortProtocolCounts();
            System.out.println("All tests passed successfully!");
        } catch (AssertionError e) {
            System.out.println("Test failed: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error occurred: " + e.getMessage());
        } finally {
            test.tearDown();
        }
    }
}