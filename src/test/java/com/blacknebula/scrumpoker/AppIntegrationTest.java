package com.blacknebula.scrumpoker;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import static com.jayway.restassured.RestAssured.given;

public abstract class AppIntegrationTest extends ApplicationTest {

    @Value("${server.address:localhost}")
    protected String serverAddress;
    @Value("${server.port:8080}")
    protected String serverPort;

    private String getBaseUrl() {
        return "http://" + serverAddress + ":" + serverPort + "/";
    }

    protected RequestSpecification givenJsonClient() {
        return defaultGiven(ContentType.JSON.withCharset("UTF-8"));
    }

    protected RequestSpecification givenBinaryClient() {
        return defaultGiven(ContentType.BINARY.withCharset("UTF-8"));
    }

    protected RequestSpecification givenHtmlClient() {
        return defaultGiven(ContentType.HTML);
    }

    private RequestSpecification defaultGiven(ContentType contentType) {
        return given().baseUri(getBaseUrl()).accept(contentType).contentType(contentType).log().ifValidationFails();
    }

    private RequestSpecification defaultGiven(String contentType) {
        return given().baseUri(getBaseUrl()).accept(contentType).contentType(contentType).log().ifValidationFails();
    }

}
