package jpm.assignment.fixparser.benchmarking;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * FYI, this is AI generated.
 */
public class FixMessageGenerator {

    private static final Random RANDOM = new Random();

    /**
     * Generates a FIX message in byte array format.
     *
     * @param messageType   The FIX message type (e.g., "D" for New Order - Single).
     * @param messageLength The approximate desired length of the message.
     * @return A FIX message as a byte array.
     */
    public static byte[] generateFixMessage(String messageType, int messageLength) {
        StringBuilder fixMessage = new StringBuilder();

        // Add standard FIX header fields
        fixMessage.append("8=FIX.4.4|");
        fixMessage.append("35=").append(messageType).append("|");
        fixMessage.append("49=SenderCompID|");
        fixMessage.append("56=TargetCompID|");
        fixMessage.append("34=").append(RANDOM.nextInt(100000)).append("|");
        fixMessage.append("52=").append(generateTimestamp()).append("|");

        // Add random fields to reach the desired length
        while (fixMessage.length() < messageLength) {
            fixMessage.append(generateRandomField());
        }

        // End with the checksum
        String fixMessageStr = fixMessage.toString();
        int checksum = calculateChecksum(fixMessageStr);
        fixMessage.append("10=").append(String.format("%03d", checksum)).append("|");

        // Replace delimiter '|' with SOH (ASCII 1) and return as byte array
        return fixMessage.toString().replace('|', '\u0001').getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Generates a random FIX field in the format of tag=value|, e.g., "55=XYZ|".
     */
    private static String generateRandomField() {
        int tag = RANDOM.nextInt(100) + 50; // Random tag between 50 and 150
        String value = generateRandomValue();
        return tag + "=" + value + "|";
    }

    /**
     * Generates a random alphanumeric value for a FIX field.
     */
    private static String generateRandomValue() {
        int length = RANDOM.nextInt(10) + 5; // Random length between 5 and 15
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) ('A' + RANDOM.nextInt(26));
            value.append(c);
        }
        return value.toString();
    }

    /**
     * Generates a timestamp in the format YYYYMMDD-HH:MM:SS.
     */
    private static String generateTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd-HH:mm:ss");
        return sdf.format(currentTimeMillis);
    }

    /**
     * Calculates the FIX checksum (sum of all ASCII values modulo 256).
     */
    private static int calculateChecksum(String message) {
        int sum = 0;
        for (int i = 0; i < message.length(); i++) {
            sum += message.charAt(i);
        }
        return sum % 256;
    }
}
