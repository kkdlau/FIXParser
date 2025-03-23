package jpm.assignment.fixparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixMessage42ParserTest {

    private FixMessage42Parser parser;

    @BeforeEach
    void setUp() {
        parser = new FixMessage42Parser();
    }

    @Test
    void testParseSingleField() {
        byte[] messageBytes = "35=A\u0001".getBytes(); // Tag 35 with value "A"

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");
        ByteArrayView field35 = parsedMessage.get(35);
        assertNotNull(field35, "Tag 35 should be present in the parsed message");
        assertEquals("A", field35.toString(), "Value of tag 35 should be 'A'");
    }

    @Test
    void testParseMultipleFields() {
        byte[] messageBytes = "8=FIX.4.2\u00019=271\u000135=8\u000134=974\u000149=TESTSELL3\u000152=20190206-16:26:09.059\u000156=TESTBUY3\u00016=174.51\u000111=141636850670842269979\u000114=100.0000000000\u000117=3636850671684357979\u000120=0\u000121=2\u000131=174.51\u000132=100.0000000000\u000137=1005448\u000138=100\u000139=2\u000140=1\u000154=1\u000155=AAPL\u000160=20190206-16:26:08.435\u0001150=2\u0001151=0.0000000000\u000110=194\u0001".getBytes();

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // Validate individual fields
        assertEquals("FIX.4.2", parsedMessage.get(8).toString(), "Value of tag 8 should be 'FIX.4.2'");
        assertEquals("271", parsedMessage.get(9).toString(), "Value of tag 9 should be 271");
        assertEquals("0.0000000000", parsedMessage.get(151).toString(), "Value of tag 151 should be 0.0000000000");
        assertEquals("1005448", parsedMessage.get(37).toString(), "Value of tag 37 should be 1005448");
    }

    @Test
    void testParseWithEmptyMessage() {
        byte[] messageBytes = "".getBytes(); // Empty message

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");
        assertNull(parsedMessage.get(8), "No tags should be present in an empty message");
    }

    @Test
    void testParseMessageWithSpacesInValue() {
        byte[] messageBytes = "8=FIX.4.2\u000149=TEST SELLER\u000156=TEST BUYER\u0001".getBytes();

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // Validate individual fields
        assertEquals("FIX.4.2", parsedMessage.get(8).toString(), "Value of tag 8 should be 'FIX.4.2'");
        assertEquals("TEST SELLER", parsedMessage.get(49).toString(), "Value of tag 49 should be 'TEST SELLER'");
        assertEquals("TEST BUYER", parsedMessage.get(56).toString(), "Value of tag 56 should be 'TEST BUYER'");
    }

    @Test
    void testParseMessageWithNumericValues() {
        byte[] messageBytes = "8=FIX.4.2\u00019=123\u000135=A\u000110=137\u0001".getBytes();

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // Validate individual fields
        assertEquals("FIX.4.2", parsedMessage.get(8).toString(), "Value of tag 8 should be 'FIX.4.2'");
        assertEquals("123", parsedMessage.get(9).toString(), "Value of tag 9 should be '123'");
        assertEquals("A", parsedMessage.get(35).toString(), "Value of tag 35 should be 'A'");
        assertEquals("137", parsedMessage.get(10).toString(), "Value of tag 10 should be '137'");
    }

    @Test
    void testParseMessageWithDuplicateTags() {
        byte[] messageBytes = "8=FIX.4.2\u000135=A\u000135=B\u0001".getBytes(); // Tag 35 appears twice

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // For duplicate tags, only the last occurrence should be preserved
        assertEquals("B", parsedMessage.get(35).toString(), "Value of tag 35 should be the last occurrence ('B')");
    }

    @Test
    void testParseMessageWithUnusualCharacters() {
        byte[] messageBytes = "8=FIX.4.2\u000135=A\u000149=TES@#$%SELL3\u0001".getBytes();

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // Validate fields with unusual characters
        assertEquals("FIX.4.2", parsedMessage.get(8).toString(), "Value of tag 8 should be 'FIX.4.2'");
        assertEquals("A", parsedMessage.get(35).toString(), "Value of tag 35 should be 'A'");
        assertEquals("TES@#$%SELL3", parsedMessage.get(49).toString(), "Value of tag 49 should be 'TES@#$%SELL3'");
    }

    @Test
    void testParsingMoreThanOneFixMessage() {
        byte[] messageBytes = "8=FIX.4.2\u000135=A\u000149=TES@#$%SELL3\u0001".getBytes();

        FixMessage42 parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // Validate fields with unusual characters
        assertEquals("FIX.4.2", parsedMessage.get(8).toString(), "Value of tag 8 should be 'FIX.4.2'");
        assertEquals("A", parsedMessage.get(35).toString(), "Value of tag 35 should be 'A'");
        assertEquals("TES@#$%SELL3", parsedMessage.get(49).toString(), "Value of tag 49 should be 'TES@#$%SELL3'");


        messageBytes = "8=FIX.4.2\u000135=A\u000135=B\u0001".getBytes(); // Tag 35 appears twice

        parsedMessage = parser.parse(messageBytes);

        // Validate the parsed message
        assertNotNull(parsedMessage, "Parsed message should not be null");

        // For duplicate tags, only the last occurrence should be preserved
        assertEquals("B", parsedMessage.get(35).toString(), "Value of tag 35 should be the last occurrence ('B')");
    }
}