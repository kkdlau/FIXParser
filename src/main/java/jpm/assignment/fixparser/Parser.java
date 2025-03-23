package jpm.assignment.fixparser;

public interface Parser<T> {
    T parse(byte[] msg);
}
