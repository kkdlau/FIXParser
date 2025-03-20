package jpm.assignment.fixparser;

public interface Parser<T> {
    T parser(byte[] msg) throws ParsingException;
}
