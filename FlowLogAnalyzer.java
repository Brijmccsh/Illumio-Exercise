import java.io.*;
import java.util.*;

public class FlowLogAnalyzer {

    private static final String FLOW_LOG_FILE = "flow_logs.txt";
    private static final String LOOKUP_TABLE_FILE = "lookup_table.csv";
    private static final String TAG_COUNT_OUTPUT = "tag_counts.csv";
    private static final String PORT_PROTOCOL_COUNT_OUTPUT = "port_protocol_counts.csv";

    private Map<String, String> lookupTable = new HashMap<>();
    private Map<String, Integer> tagCounts = new HashMap<>();
    private Map<String, Integer> portProtocolCounts = new HashMap<>();

    public static void main(String[] args) {
        FlowLogAnalyzer analyzer = new FlowLogAnalyzer();
        analyzer.run();
    }

    public void run() {
        loadLookupTable();
        processFlowLogs();
        writeTagCounts();
        writePortProtocolCounts();
    }

    private String getProtocolName(String protocolNumber) {
        switch (protocolNumber) {
            case "6": return "tcp";
            case "17": return "udp";
            case "1": return "icmp";
            default: return protocolNumber;
        }
    }

    private void loadLookupTable() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOOKUP_TABLE_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = parts[0].toLowerCase() + "," + parts[1].toLowerCase();
                    lookupTable.put(key, parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processFlowLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FLOW_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 14) {
                    String dstPort = parts[6];
                    String protocol = parts[8];
                    String key = dstPort.toLowerCase() + "," + protocol.toLowerCase();
                    
                    String tag = lookupTable.getOrDefault(key, "Untagged");
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                    
                    String portProtocolKey = dstPort + "," + protocol;
                    portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTagCounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TAG_COUNT_OUTPUT))) {
            writer.println("Tag,Count");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePortProtocolCounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PORT_PROTOCOL_COUNT_OUTPUT))) {
            writer.println("Port,Protocol,Count");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                String[] parts = entry.getKey().split(",");
                String protocolName = getProtocolName(parts[1]);
                writer.println(parts[0] + "," + protocolName + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}