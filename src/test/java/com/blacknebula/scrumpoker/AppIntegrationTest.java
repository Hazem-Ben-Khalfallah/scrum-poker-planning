package com.blacknebula.scrumpoker;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.springframework.boot.test.IntegrationTest;

import static com.jayway.restassured.RestAssured.given;

@IntegrationTest
public abstract class AppIntegrationTest extends ApplicationTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    protected String getBaseUrl() {
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
