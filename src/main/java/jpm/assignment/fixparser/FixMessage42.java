package jpm.assignment.fixparser;

import java.util.BitSet;

public class FixMessage42 {
    private static final int NUM_FIELDS = 446;
    private final BitSet availableFields;
    private final ByteArrayView[] fields;

    public FixMessage42() {
        this.availableFields = new BitSet(FixMessage42.NUM_FIELDS);
        this.fields = new ByteArrayView[FixMessage42.NUM_FIELDS];
        initializeFields();
    }

    public FixMessage42(FixMessage42 msg) {
        this.availableFields = (BitSet) msg.availableFields.clone();
        this.fields = new ByteArrayView[FixMessage42.NUM_FIELDS];
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
    }

    public void setField(int fieldIndex, byte[] chars, int start, int length) {
        final ByteArrayView view = this.fields[fieldIndex];
        view.setChars(chars);
        view.setStart(start);
        view.setLength(length);
        availableFields.set(fieldIndex, true);
    }

    public ByteArrayView get(int fieldIndex) {
        if (availableFields.get(fieldIndex)) {
            return this.fields[fieldIndex];
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
