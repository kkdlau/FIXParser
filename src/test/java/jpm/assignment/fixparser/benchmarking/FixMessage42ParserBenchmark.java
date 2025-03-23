package jpm.assignment.fixparser.benchmarking;

import jpm.assignment.fixparser.ByteArrayView;
import jpm.assignment.fixparser.FixMessage42Parser;
import jpm.assignment.fixparser.FixMessage42;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class FixMessage42ParserBenchmark {

    @Test
    public void testBenchmarkParseOnly() {
        int numOfIterations = 10000;
        int numOfWarmupIterations = 5;
        int messageLengthLowerBound = 256;

        {
            FixMessage42Parser parser = new FixMessage42Parser();

            System.out.println("Starting benchmark for Fix42Parser...");
            System.out.println("Executing iterations for warmup: " + numOfWarmupIterations);

            benchmarkParseOnly(numOfWarmupIterations, messageLengthLowerBound, parser);

            System.out.println("Warmup completed.");
            System.out.println("Executing iterations for benchmarking: " + numOfIterations);

            long startTime = System.nanoTime();
            benchmarkParseOnly(numOfIterations, messageLengthLowerBound, parser);
            long endTime = System.nanoTime();


            long elapsedTime = endTime - startTime;
            System.out.println("Benchmark completed.");
            System.out.println("Total time taken: " + (elapsedTime / 1_000_000) + " ms");
            System.out.println("Average time per message: " + (elapsedTime / numOfIterations) + " ns");
        }
    }

    @Test
    public void testBenchmarkParseAndConsume() {
        int numOfIterations = 10000;
        int numOfWarmupIterations = 5;
        int messageLengthLowerBound = 256;

        {
            FixMessage42Parser parser = new FixMessage42Parser();

            System.out.println("Starting benchmark for Fix42Parser...");
            System.out.println("Executing iterations for warmup: " + numOfWarmupIterations);

            benchmarkParseOnly(numOfWarmupIterations, messageLengthLowerBound, parser);

            System.out.println("Warmup completed.");
            System.out.println("Executing iterations for benchmarking: " + numOfIterations);

            long startTime = System.nanoTime();
            benchmarkParseAndConsume(numOfIterations, messageLengthLowerBound, parser);
            long endTime = System.nanoTime();


            long elapsedTime = endTime - startTime;
            System.out.println("Benchmark completed.");
            System.out.println("Total time taken: " + (elapsedTime / 1_000_000) + " ms");
            System.out.println("Average time per message: " + (elapsedTime / numOfIterations) + " ns");
        }
    }

    private static void benchmarkParseOnly(int numOfIterations, int messageLengthThreshold, FixMessage42Parser parser) {
        FixMessage42 parsed = null;
        for (int i = 0; i < numOfIterations; i++) {
            byte[] fixMessage = FixMessageGenerator.generateFixMessage("D", messageLengthThreshold);
            parsed = parser.parse(fixMessage);
        }
        sideEffectMethodToPreventOptimization(parsed);
    }

    private static void benchmarkParseAndConsume(int numOfIterations, int messageLengthThreshold, FixMessage42Parser parser) {
        FixMessage42 parsed = null;
        Random random = new Random();

        for (int i = 0; i < numOfIterations; i++) {
            byte[] fixMessage = FixMessageGenerator.generateFixMessage("D", messageLengthThreshold);
            parsed = parser.parse(fixMessage);
            consume(parsed, random);
        }

        sideEffectMethodToPreventOptimization(parsed);
    }

    private static void consume(FixMessage42 parsed, Random random) {
        final int fieldIndex = random.nextInt(100);
        final ByteArrayView byteArrayView = parsed.get(fieldIndex);
        if (byteArrayView != null && byteArrayView.charAt(0) == '_')
            System.out.println();

    }

    // idea: http://github.com/jbloch/effective-java-3e-source-code/blob/master/src/effectivejava/chapter2/item6/Sum.java
    private static void sideEffectMethodToPreventOptimization(FixMessage42 parsed) {
        // Prevents VM from optimizing away everything.
        if (parsed.get(8).charAt(0) == '_')
            System.out.println();
    }
}