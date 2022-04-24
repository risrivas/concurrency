package org.paumard.parallelstream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class M04_Sum {

    private Random random = new Random();

    @Param({"10000000"})
    private int N;
    private List<Integer> intsN100;

    @Setup
    public void setup() {
        intsN100 = IntStream.range(0, N)
                .mapToObj(index -> random.nextInt(100))
                .collect(Collectors.toList());
    }

    @Benchmark
    public double sum_no_parallel() {
        return intsN100.stream().mapToInt(i -> i).sum();
    }

    @Benchmark
    public double sum_parallel() {
        return intsN100.stream().mapToInt(i -> i).parallel().sum();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(M04_Sum.class.getName())
                .build();
        new Runner(opt).run();
    }
}
