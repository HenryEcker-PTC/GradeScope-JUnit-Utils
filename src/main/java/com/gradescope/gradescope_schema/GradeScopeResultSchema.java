package com.gradescope.gradescope_schema;

import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class GradeScopeResultSchema implements JSONable {
    private String output;
    private GradeScopeOutputFormat outputFormat;
    private GradeScopeOutputFormat testOutputFormat;
    private GradeScopeOutputFormat testNameFormat;
    private GradeScopeVisibility visibility;
    private GradeScopeVisibility stdoutVisibility;
    private Long executionTime;
    private List<JSONObject> tests;

    public GradeScopeResultSchema() {
        this.output = null;
        this.outputFormat = null;
        this.testOutputFormat = null;
        this.testNameFormat = null;
        this.visibility = null;
        this.stdoutVisibility = null;
        this.executionTime = null;
        this.tests = null;
    }


    public GradeScopeResultSchema withOutput(String output) {
        this.output = output;
        return this;
    }

    public GradeScopeResultSchema withoutOutput() {
        this.output = null;
        return this;
    }

    public GradeScopeResultSchema withOutputFormat(GradeScopeOutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public GradeScopeResultSchema withoutOutputFormat() {
        this.outputFormat = null;
        return this;
    }

    public GradeScopeResultSchema withTestOutputFormat(GradeScopeOutputFormat testOutputFormat) {
        this.testOutputFormat = testOutputFormat;
        return this;
    }

    public GradeScopeResultSchema withoutTestOutputFormat() {
        this.testOutputFormat = null;
        return this;
    }

    public GradeScopeResultSchema withTestNameFormat(GradeScopeOutputFormat testNameFormat) {
        this.testNameFormat = testNameFormat;
        return this;
    }

    public GradeScopeResultSchema withoutTestNameFormat() {
        this.testNameFormat = null;
        return this;
    }

    public GradeScopeResultSchema withVisibility(GradeScopeVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public GradeScopeResultSchema withoutVisibility() {
        this.visibility = null;
        return this;
    }

    public GradeScopeResultSchema withStdoutVisibility(GradeScopeVisibility stdoutVisibility) {
        this.stdoutVisibility = stdoutVisibility;
        return this;
    }

    public GradeScopeResultSchema withoutStdoutVisibility() {
        this.stdoutVisibility = null;
        return this;
    }

    public GradeScopeResultSchema withExecutionTime(long executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public GradeScopeResultSchema withoutExecutionTime() {
        this.executionTime = null;
        return this;
    }

    public GradeScopeResultSchema withTests(List<TestResultSchema> testResultSchemas) {
        this.tests = testResultSchemas.stream()
                .map(TestResultSchema::toJSON)
                .collect(Collectors.toList());
        return this;
    }

    public GradeScopeResultSchema withoutTests() {
        this.tests = null;
        return this;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("execution_time", this.executionTime);
        if (GradeScopeSchemaUtils.isDefined(this.output)) {
            json.put("output", this.output);
        }
        if (GradeScopeSchemaUtils.isDefined(this.outputFormat)) {
            json.put("output_format", this.outputFormat);
        }
        if (GradeScopeSchemaUtils.isDefined(this.testOutputFormat)) {
            json.put("test_output_format", this.testOutputFormat);
        }
        if (GradeScopeSchemaUtils.isDefined(this.testNameFormat)) {
            json.put("test_name_format", this.testNameFormat);
        }
        if (GradeScopeSchemaUtils.isDefined(this.visibility)) {
            json.put("visibility", this.visibility);
        }
        if (GradeScopeSchemaUtils.isDefined(this.stdoutVisibility)) {
            json.put("stdout_visibility", this.stdoutVisibility);
        }
        if (GradeScopeSchemaUtils.isDefined(this.tests)) {
            json.put("tests", this.tests);
        }
        return json;
    }

    public String toJSONString() {
        return this.toJSON().toString();
    }
}
