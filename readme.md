## ByteArrayView
A lightweight wrapper around a byte array that implements the CharSequence interface. It allows viewing portions of a byte array without copying data, but not modifying the byte array. That's why this is a "view-only" class.

Key Features:
- Zero-copy handling of byte arrays. Only reference to byte array.
- Compared to `ByteBuffer`, this class is reusable by setting the member variables.
  - And I think `ByteBuffer` will copy the byte array in the constructor.

Assumption:
- The input must be ASCII-encoded string.

## FixMessage42
A class representing a FIX 4.2 message. It stores fields using preallocated data structures for efficient access and manipulation.

Key Features:
- Used `BitSet` to track available fields.
  - `BitSet` uses single bit to track a field, which minimize the memory needed.
- Leveraged `ByteArrayView` objects to store field values.
  - Therefore, no copying needed.
- Used to two arrays `fieldIndices` and `fields` to store the field values, and that is cache-friendly.
  - All available fields stored in a contiguous way.
- Avoid using `HashMap` to retrieve values, because `HashMap` has some cost on hashing and value lookup.

## FixMessage42Parser
A parser for FIX 4.2 messages. It processes the byte array in a single pass, extracting tags and values without intermediate object creation.

Key Features:
- Single-pass parsing for high efficiency. No look back.


## Example

```java
public class FixMessageParserExample {
    public static void main(String[] args) {
        byte[] fixMessage = "8=FIX.4.2\u00019=12\u000135=D\u000149=SenderCompID\u000156=TargetCompID\u000110=185\u0001".getBytes();
        FixMessage42Parser parser = new FixMessage42Parser();

        try {
            FixMessage42 message = parser.parse(fixMessage);

            // Access field values
            // Note: toString() will create a string object. Instead, we can access individual character, which is more efficient.
            System.out.println("BeginString: " + message.get(8).toString());
            System.out.println("BodyLength: " + message.get(9).toString());
            System.out.println("MsgType: " + message.get(35).toString());
        } catch (FixMessage42Parser.FixParsingException e) {
            System.err.println("Error parsing FIX message: " + e.getMessage());
        }
    }
}
```

## Benchmarking

See `FixMessage42ParserBenchmark.java`.

### Result

- Machine: Macbook Air M2

---

Starting benchmark for Fix42Parser (Parse & Consume)...

Executing iterations for warmup: 5

Warmup completed.

Executing iterations for benchmarking: 10000

Benchmark completed.

Total time taken: 171 ms

Average time per message: 17197 ns

---

Starting benchmark for Fix42Parser (Parse Only)...

Executing iterations for warmup: 5

Warmup completed.

Executing iterations for benchmarking: 10000

Benchmark completed.

Total time taken: 110 ms

Average time per message: 11011 ns
