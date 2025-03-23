package jpm.assignment.fixparser;

public enum FixTag {
    BeginString(8),
    BodyLength(9),
    MsgType(35),
    SenderCompID(49),
    TargetCompID(56),
    MsgSeqNum(34),
    SendingTime(52),
    CheckSum(10),
    // Common trade-related tags
    ClOrdID(11),
    OrderID(37),
    OrdType(40),
    Price(44),
    Side(54),
    Symbol(55);

    private final int number;

    FixTag(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
