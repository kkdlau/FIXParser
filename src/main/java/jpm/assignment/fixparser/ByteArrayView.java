package jpm.assignment.fixparser;
import java.util.stream.IntStream;

/**
 * The ByteArrayView class provides a lightweight wrapper for a byte array
 * that enables viewing and manipulating a portion of the byte array as
 * a character sequence. It implements the CharSequence interface, allowing
 * it to be used in contexts that require CharSequence objects.
 * <p>
 * This class assumes the input byte array is ASCII-encoded.
 * <p>
 * Basically a mirror implementation of std::string_view.
 */
public class ByteArrayView implements CharSequence {
    private static final UnsupportedOperationException UNSUPPORTED = new UnsupportedOperationException("UNSUPPORTED");
    private byte[] bytes;
    private int start;
    private int length;
    private StringBuffer stringBuffer;

    /**
     * Constructs a new ByteArrayView over the specified portion of the input byte array.
     *
     * @param bytes the byte array to wrap. Assumed to be ASCII-encoded.
     * @param start the starting index (inclusive) of the view within the byte array.
     * @param length the length of the view (number of bytes to consider).
     */
    public ByteArrayView(byte[] bytes, int start, int length) {
        this.bytes = bytes;
        this.start = start;
        this.length = length;
    }

    public ByteArrayView(ByteArrayView view) {
        this.bytes = new byte[view.bytes.length];
        System.arraycopy(view.bytes, 0, this.bytes, 0, view.bytes.length);
        this.start = view.start;
        this.length = view.length;
        this.stringBuffer = view.stringBuffer;
    }

    public void setChars(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        return (char)(this.bytes[this.start + index] & 0xFF);
    }

    @Override
    public boolean isEmpty() {
        return this.bytes == null || this.length <= 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end > length || start > end) {
            throw new IndexOutOfBoundsException("Start: " + start + ", End: " + end + ", Length: " + length);
        }
        return new ByteArrayView(this.bytes, this.start + start, end - start + 1);
    }

    @Override
    public IntStream chars() {
        throw UNSUPPORTED;
    }

    @Override
    public IntStream codePoints() {
        throw UNSUPPORTED;
    }

    /**
     * Returns the string representation of the current view.
     * You can work with this class using charAt, which is more efficient and no copying.
     *
     * @return the string representation of the current view.
     */
    @Override
    public String toString() {
       return (new StringBuffer(this)).toString();
    }
}
