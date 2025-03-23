package jpm.assignment.fixparser;

import java.util.BitSet;

public class FixMessage42 {
    private static final int NUM_FIELDS = 446;
    private static final int NUM_POSSIBLE_FIELDS_IN_SINGLE_MSG = 256;
    private final BitSet availableFields;
    private final ByteArrayView[] fields;
    private final int[] fieldIndices;
    private int numFields;

    public FixMessage42() {
        this.availableFields = new BitSet(FixMessage42.NUM_FIELDS);
        this.fields = new ByteArrayView[FixMessage42.NUM_POSSIBLE_FIELDS_IN_SINGLE_MSG];
        this.fieldIndices = new int[FixMessage42.NUM_FIELDS];
        numFields = 0;
        initializeFields();
    }

    public FixMessage42(FixMessage42 msg) {
        this.availableFields = (BitSet) msg.availableFields.clone();
        this.fields = new ByteArrayView[FixMessage42.NUM_FIELDS];
        this.fieldIndices = msg.fieldIndices;
        copyFixFields(msg);
    }

    private void copyFixFields(FixMessage42 msg) {
        for (int i = 0; i < this.fields.length; i++) {
            final ByteArrayView fieldToCopy = msg.fields[i];
            if (fieldToCopy.isEmpty()) continue;

            fields[i] = new ByteArrayView(fieldToCopy);
        }
    }

    private void initializeFields() {
        for (int i = 0; i < this.fields.length; i++) {
            this.fields[i] = new ByteArrayView(null, -1, -1);
        }
    }

    public void clear() {
        this.availableFields.clear();
        this.numFields = 0;
    }

    public void setField(int tag, byte[] chars, int start, int length) {
        final ByteArrayView view = this.fields[numFields];
        view.setChars(chars);
        view.setStart(start);
        view.setLength(length);
        availableFields.set(tag, true);
        fieldIndices[tag] = numFields;
        ++numFields;
    }

    public ByteArrayView get(int tag) {
        if (availableFields.get(tag)) {
            return this.fields[fieldIndices[tag]];
        } else {
            return null;
        }
    }

    public boolean hasField(int tag) {
        return availableFields.get(tag);
    }

    public boolean isValid() {
        return hasField(FixTag.BeginString.getNumber()) &&
                hasField(FixTag.BodyLength.getNumber()) &&
                hasField(FixTag.MsgType.getNumber()) &&
                hasField(FixTag.MsgSeqNum.getNumber()) &&
                hasField(FixTag.CheckSum.getNumber());
    }
}
