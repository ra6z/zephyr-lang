package io.ra6.zephyr.evaluating;

public class EvaluationResult {
    private final int exitCode;

    public EvaluationResult(int exitCode) {
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
