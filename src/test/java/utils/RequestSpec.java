package utils;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpec {

    @Step("Get Common Request Specification (Base URI, JSON Content-Type)")
    public static RequestSpecification getCommonSpec(){
        return new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .addHeader("Content-Type","application/json")
                .build();
    }
}
