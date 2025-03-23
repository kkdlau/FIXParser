package jpm.assignment.fixparser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ByteArrayViewTest {

    @Test
    void testLength() {
        byte[] bytes = "TestMessage".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);
        assertEquals(11, view.length());
    }

    @Test
    void testCharAt() {
        byte[] bytes = "HelloWorld".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);
        assertEquals('H', view.charAt(0));
        assertEquals('W', view.charAt(5));
        assertEquals('d', view.charAt(9));
    }

    @Test
    void testIsEmpty() {
        byte[] bytes = "NotEmpty".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);
        assertFalse(view.isEmpty());

        ByteArrayView emptyView = new ByteArrayView(bytes, 0, 0);
        assertTrue(emptyView.isEmpty());
    }

    @Test
    void testSubSequence() {
        byte[] bytes = "SubSequenceTest".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);

        CharSequence subSequence = view.subSequence(3, 10);
        assertEquals("Sequence", subSequence.toString());
    }

    @Test
    void testToString() {
        byte[] bytes = "ByteArrayViewTest".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);
        assertEquals("ByteArrayViewTest", view.toString());
    }

    @Test
    void testSetters() {
        byte[] bytes = "Original".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);

        byte[] newBytes = "Updated".getBytes();
        view.setChars(newBytes);
        view.setStart(1);
        view.setLength(5);

        assertEquals("pdate", view.toString());
    }

    @Test
    void testUnsupportedOperations() {
        byte[] bytes = "Unsupported".getBytes();
        ByteArrayView view = new ByteArrayView(bytes, 0, bytes.length);

        assertThrows(UnsupportedOperationException.class, view::chars);
        assertThrows(UnsupportedOperationException.class, view::codePoints);
    }

    @Test
    void testCopyConstructor() {
        byte[] bytes = "CopyTest".getBytes();
        ByteArrayView original = new ByteArrayView(bytes, 0, bytes.length);

        ByteArrayView copy = new ByteArrayView(original);
        assertEquals(original.toString(), copy.toString());

        // Modify the original to ensure copy is independent
        original.setChars("Modified".getBytes());
        assertNotEquals(original.toString(), copy.toString());
    }
}