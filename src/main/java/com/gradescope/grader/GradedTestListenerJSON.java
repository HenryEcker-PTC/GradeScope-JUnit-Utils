//adapted from http://memorynotfound.com/add-junit-listener-example/
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

    /* Code to run at the beginning of a test run. */
    public void testRunStarted(Description description) throws Exception {
        allTestResultSchemas = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    /* Code to run at the end of test run. */
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
        /* Capture StdOut (both ours and theirs) so that we can relay it to the students. */
        currentTestResultSchema = new TestResultSchema(gradedTestAnnotation);

        /* By default, every test passes. */
        currentTestResultSchema.setScore(gradedTestAnnotation.max_score());

        capturedData = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedData));
    }

    /**
     * When a test completes, add the test output at the bottom. Then stop capturing
     * StdOut. Open question: Is putting the captured output at the end clear? Or is that
     * possibly confusing? We'll see...
     */
    public void testFinished(Description description) throws Exception {
        String capturedDataString = capturedData.toString();
        if (capturedDataString.length() > 0) {
//            currentTestResult.addOutput("Captured Test Output: \n");
            if (capturedDataString.length() > MAX_OUTPUT_LENGTH) {
                capturedDataString = capturedDataString.substring(0, MAX_OUTPUT_LENGTH) +
                        "... truncated due to excessive output!";
            }
            currentTestResultSchema.addOutput(capturedDataString);
        }
        System.setOut(STDOUT);

        /* For Debugging. */
        if (false) {
            System.out.println(currentTestResultSchema);
        }

        allTestResultSchemas.add(currentTestResultSchema);
    }

    /**
     * Sets score to 0 and appends reason for failure and dumps a stack trace.
     * Other possible things we might want to consider including: http://junit.sourceforge.net/javadoc/org/junit/runner/notification/Failure.html.
     */
    public void testFailure(Failure failure) throws Exception {
        currentTestResultSchema.setScore(0);
        currentTestResultSchema.addOutput("Test Failed: ");
        currentTestResultSchema.addOutput(JUnitUtilities.failureToString(failure));
        currentTestResultSchema.addOutput("\n");
        //currentTestResult.addOutput(failure.getTrace());
    }

}