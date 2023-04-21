module com.gradescope {
    requires junit;
    requires org.json;

    exports com.gradescope.grader;
    exports com.gradescope.gradescope_schema;
    exports com.gradescope.junit;
}