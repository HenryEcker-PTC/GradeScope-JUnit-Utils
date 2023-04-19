package com.gradescope.gradescope_schema;

import com.gradescope.grader.GradedTest;
import org.json.JSONObject;

public class TestResultSchema implements JSONable {
    private double score;
    private final double maxScore;
    private final String name;
    private final GradeScopeOutputFormat nameFormat;
    private final String number;
    private final GradeScopeOutputFormat outputFormat;
    private final String[] tags;
    private final GradeScopeVisibility visibility;
    private final StringBuilder outputSB;


    public TestResultSchema(
            double maxScore,
            String name,
            GradeScopeOutputFormat nameFormat,
            String number,
            GradeScopeOutputFormat outputFormat,
            String[] tags,
            GradeScopeVisibility visibility
    ) {
        this.maxScore = maxScore;
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
        json.put("score", this.score);
        json.put("max_score", this.maxScore);
        json.put("name", this.name);
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
