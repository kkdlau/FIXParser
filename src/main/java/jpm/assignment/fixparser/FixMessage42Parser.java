package jpm.assignment.fixparser;

public class FixMessage42Parser implements Parser<FixMessage42> {
    private static final byte SOH = 0x01;  // ASCII Start of Header character
    private static final byte EQUALS = (byte) '=';
    FixMessage42 parsedMessage = new FixMessage42();
    int tagBuffer = 0;

    /**
     * Constructs the tag value from individual bytes without creating intermediate objects.
     *
     * @param tagChar The byte representing a single digit of the tag value.
     */
    private void constructTag(byte tagChar) {
        if (tagChar < '0' || tagChar > '9') {
            throw new FixParsingException("Invalid character in tag: " + (char) tagChar);
        }
        tagBuffer = tagBuffer * 10 + (tagChar - '0');
    }

    /**
     * Parses a FIX 4.2 message encoded as a byte array.
     *
     * @param message The byte array containing the FIX message.
     * @return A parsed FixMessage42 object containing tag-value pairs.
     * @throws FixParsingException If the message is malformed or contains invalid data.
     */
    @Override
    public FixMessage42 parse(byte[] message) {
        int i = 0;
        parsedMessage.clear();
        try {
            while (i < message.length) {
                tagBuffer = 0;

                // Extract tag
                while (i < message.length && message[i] != EQUALS) {
                    constructTag(message[i]);
                    ++i;
                }

                if (i >= message.length || message[i] != EQUALS) {
                    throw new FixParsingException("Missing '=' after tag at position " + i);
                }

                int tag = tagBuffer;
                ++i; // Skip the equal sign

                // Extract the value
                int valueStart = i;
                while (i < message.length && message[i] != SOH) {
                    i++;
                }

                if (i >= message.length || message[i] != SOH) {
                    throw new FixParsingException("Missing SOH after value for tag " + tag);
                }

                parsedMessage.setField(tag, message, valueStart, i - valueStart);

                if (i < message.length) {
                    i++; // Skip SOH
                }
            }
        } catch (FixParsingException e) {
            throw e; // Rethrow if the caller needs to handle it
        } catch (Exception e) {
            throw new FixParsingException("Unexpected error during parsing", e);
        }
        return parsedMessage;
    }

    /**
     * Custom exception for FIX parsing errors.
     */
    public static class FixParsingException extends RuntimeException {
        public FixParsingException(String message) {
            super(message);
        }

        public FixParsingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}