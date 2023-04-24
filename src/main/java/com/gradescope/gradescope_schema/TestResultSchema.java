package com.gradescope.gradescope_schema;

import org.json.JSONObject;

public class TestResultSchema implements JSONable {
    private Double score;
    private final Double maxScore;
    private final GradeScopeStatus status;
    private final String name;
    private final GradeScopeOutputFormat nameFormat;
    private final String number;
    private final GradeScopeOutputFormat outputFormat;
    private final String[] tags;
    private final GradeScopeVisibility visibility;
    private final StringBuilder outputSB;


    public TestResultSchema(
            Double maxScore,
            GradeScopeStatus status,
            String name,
            GradeScopeOutputFormat nameFormat,
            String number,
            GradeScopeOutputFormat outputFormat,
            String[] tags,
            GradeScopeVisibility visibility
    ) {
        /* By default, every test passes. */
        this.score = maxScore;
        this.maxScore = maxScore;
        this.status = status;
        this.name = name;
        this.nameFormat = nameFormat;
        this.number = number;
        this.outputFormat = outputFormat;
        this.tags = tags;
        this.visibility = visibility;
        this.outputSB = new StringBuilder();
    }

    public TestResultSchema(GradedTest testAnnotation) {
        this(
                testAnnotation.max_score(),
                testAnnotation.status(),
                testAnnotation.name(),
                testAnnotation.name_format(),
                testAnnotation.number(),
                testAnnotation.output_format(),
                testAnnotation.tags(),
                testAnnotation.visibility()
        );
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addOutput(String x) {
        outputSB.append(x);
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        if (GradeScopeSchemaUtils.isDefined(this.score)) {
            json.put("score", this.score);
        }
        if (GradeScopeSchemaUtils.isDefined(this.maxScore)) {
            json.put("max_score", this.maxScore);
        }
        if (GradeScopeSchemaUtils.isDefined(this.status)) {
            json.put("status", this.status);
        }
        if (GradeScopeSchemaUtils.isDefined(this.name)) {
            json.put("name", this.name);
        }
        if (GradeScopeSchemaUtils.isDefined(this.nameFormat)) {
            json.put("name_format", this.nameFormat);
        }
        if (GradeScopeSchemaUtils.isDefined(this.number)) {
            json.put("number", this.number);
        }
        String output = this.outputSB.toString();
        if (GradeScopeSchemaUtils.isDefined(output)) {
            json.put("output", output);
        }
        if (GradeScopeSchemaUtils.isDefined(this.outputFormat)) {
            json.put("output_format", this.outputFormat);
        }
        if (GradeScopeSchemaUtils.isDefined(this.tags)) {
            json.put("tags", this.tags);
        }
        if (GradeScopeSchemaUtils.isDefined(this.visibility)) {
            json.put("visibility", this.visibility);
        }
        return json;
    }

    public String toJSONString() {
        return this.toJSON().toString();
    }
}
