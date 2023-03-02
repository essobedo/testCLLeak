package org.example;

import io.restassured.RestAssured;

import static org.hamcrest.CoreMatchers.is;

public class Main {
    public static void callTest() {
        RestAssured.given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }
}