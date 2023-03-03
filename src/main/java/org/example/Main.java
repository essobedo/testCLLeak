package org.example;

import io.restassured.RestAssured;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.InvokerHelper;

import static org.hamcrest.CoreMatchers.is;

public class Main {
    public static void callTest() {
        RestAssured.given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    public static void clean() {
        for (ClassInfo ci : ClassInfo.getAllClassInfo()) {
            InvokerHelper.removeClass(ci.getTheClass());
        }
    }
}