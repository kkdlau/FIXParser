package jpm.assignment.fixparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixMessage42Test {

    private FixMessage42 fixMessage;

    @BeforeEach
    void setUp() {
        fixMessage = new FixMessage42();
    }

    @Test
    void testSetFieldAndGetField() {
        byte[] messageBytes = "8=FIX.4.2\u00019=74\u000135=A\u000134=978\u000149=TESTSELL3\u000152=20190206-16:29:19.208\u000156=TESTBUY3\u000198=0\u0001108=60\u000110=137\u0001".getBytes();
        int fieldIndex = 9;
        int start = 12;
        int length = 2;

        // Set field
        fixMessage.setField(fieldIndex, messageBytes, start, length);

        // Retrieve field
        ByteArrayView field = fixMessage.get(fieldIndex);

        assertNotNull(field, "Field should not be null after being set");
        assertEquals(length, field.length(), "Field length should match the specified length");
        assertEquals("74", field.toString(), "Field content should match the expected value");
    }

    @Test
    void testGetUnsetField() {
        int fieldIndex = 8; // Field hasn't been set

        ByteArrayView field = fixMessage.get(fieldIndex);

        assertNull(field, "Field should be null if it hasn't been set");
    }

    @Test
    void testClearFields() {
        byte[] messageBytes = "8=FIX.4.2\u00019=74\u000135=A\u000134=978\u000149=TESTSELL3\u000152=20190206-16:29:19.208\u000156=TESTBUY3\u000198=0\u0001108=60\u000110=137\u0001".getBytes();
        int fieldIndex = 9;
        int start = 12;
        int length = 2;

        // Set field
        fixMessage.setField(fieldIndex, messageBytes, start, length);
        assertNotNull(fixMessage.get(fieldIndex), "Field should not be null after being set");

        // Clear all fields
        fixMessage.clear();

        // Ensure field is now null
        ByteArrayView field = fixMessage.get(fieldIndex);
        assertNull(field, "Field should be null after clearing all fields");
    }

    @Test
    void testSetMultipleFieldsAndRetrieve() {
        byte[] messageBytes = "8=FIX.4.2\u00019=74\u000135=A\u000134=978\u000149=TESTSELL3\u000152=20190206-16:29:19.208\u000156=TESTBUY3\u000198=0\u0001108=60\u000110=137\u0001".getBytes();

        // Set multiple fields
        fixMessage.setField(9, messageBytes, 12, 2); // Field 9: "74"
        fixMessage.setField(35, messageBytes, 18, 1); // Field 35: "A"
        fixMessage.setField(49, messageBytes, 30, 9); // Field 49: "TESTSELL3"

        // Retrieve and validate each field
        assertEquals("74", fixMessage.get(9).toString(), "Field 9 content should match '74'");
        assertEquals("A", fixMessage.get(35).toString(), "Field 35 content should match 'A'");
        assertEquals("TESTSELL3", fixMessage.get(49).toString(), "Field 49 content should match 'TESTSELL3'");
    }
}