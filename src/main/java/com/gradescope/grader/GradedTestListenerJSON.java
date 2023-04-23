package com.gradescope.grader;

import com.gradescope.gradescope_schema.GradeScopeResultSchema;
import com.gradescope.gradescope_schema.TestResultSchema;
import com.gradescope.junit.JUnitUtilities;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradedTestListenerJSON extends RunListener {

    private static final int MAX_OUTPUT_LENGTH = 8192;

    /**
     * Storage of print output that has been intercepted.
     */
    private static ByteArrayOutputStream capturedData = new ByteArrayOutputStream();
    /**
     * Tracks original StdOut for when capturing end
     */
    private static final PrintStream STDOUT = System.out;

    /* Current test result. Created at the beginning of every test, completed at the
       end of every test. */
    private static TestResultSchema currentTestResultSchema;

    /* All test results. */
    private static List<TestResultSchema> allTestResultSchemas;

    /* Test run start time. */
    private static long startTime;

    private final GradeScopeResultSchema globalGradeScopeSchemaConfig;

    public GradedTestListenerJSON(GradeScopeResultSchema globalGradeScopeSchemaConfig) {
        this.globalGradeScopeSchemaConfig = globalGradeScopeSchemaConfig;
    }

    /* Test Suite Start */
    public void testRunStarted(Description description) throws Exception {
        allTestResultSchemas = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    /* Test Suite End */
    public void testRunFinished(Result result) throws Exception {
        /* Dump allTestResults to StdOut in JSON format. */
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println(
                globalGradeScopeSchemaConfig
                        .withExecutionTime(elapsed)
                        .withTests(allTestResultSchemas)
                        .toJSONString()
        );
    }

    /* Each Test Start */
    public void testStarted(Description description) throws Exception {
        Optional<Annotation> filteredAnnotations = description
                .getAnnotations()
                .stream()
                .filter(GradedTest.class::isInstance)
                .findFirst();
        if (filteredAnnotations.isEmpty()) {
            throw new RuntimeException("GradedTest Annotation Expected on all tests");
        }
        GradedTest gradedTestAnnotation = (GradedTest) filteredAnnotations.get();
        currentTestResultSchema = new TestResultSchema(gradedTestAnnotation);

        /* Capture StdOut (both ours and theirs) so that we can relay it to the students. */
        capturedData = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedData));
    }

    /* Each Test End */
    public void testFinished(Description description) throws Exception {
        String capturedDataString = capturedData.toString();
        if (capturedDataString.length() > 0) {
            if (capturedDataString.length() > MAX_OUTPUT_LENGTH) {
                capturedDataString = capturedDataString.substring(0, MAX_OUTPUT_LENGTH) +
                        "... truncated due to excessive output!";
            }
            currentTestResultSchema.addOutput(capturedDataString);
        }

        System.setOut(STDOUT);

        allTestResultSchemas.add(currentTestResultSchema);
    }

    /* On Failure */
    public void testFailure(Failure failure) throws Exception {
        currentTestResultSchema.setScore(0);
        currentTestResultSchema.addOutput("Test Failed: ");
        currentTestResultSchema.addOutput(JUnitUtilities.failureToString(failure));
        currentTestResultSchema.addOutput("\n");
        //currentTestResult.addOutput(failure.getTrace());
    }

}