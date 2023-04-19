package com.gradescope.gradescope_schema;

import org.json.JSONObject;

public interface JSONable {
    JSONObject toJSON();
    String toJSONString();
}
