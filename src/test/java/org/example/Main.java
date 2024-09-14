package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Set base URI
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // Build the request and make the API call
        String response = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{" +
                        "\"location\": {" +
                        "\"lat\": -38.383494," +
                        "\"lng\": 33.427362" +
                        "}," +
                        "\"accuracy\": 50," +
                        "\"name\": \"Frontline house\"," +
                        "\"phone_number\": \"(+91) 983 893 3937\"," +
                        "\"address\": \"29, side layout, cohen 09\"," +
                        "\"types\": [" +
                        "\"shoe park\"," +
                        "\"shop\"" +
                        "]," +
                        "\"website\": \"http://google.com\"," +
                        "\"language\": \"French-IN\"" +
                        "}")
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))



        System.out.println(response);
        JsonPath js = new JsonPath(response);
        String place_id = js.getString("place_id");
        System.out.println(place_id);


//Updating place id obtained above
        String newAddress = "Summer Walk, Africa";
        given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{" +
                        "\"place_id\": \"" + place_id + "\"," +
                        "\"address\":\"" + newAddress + "\",\r\n" +
                        "\"key\": \"qaclick123\"" +
                        "}")
                .when()
                .put("/maps/api/place/update/json")
                .then()
                .assertThat()
                .log().all()
                .statusCode(200)
                .body("msg", equalTo("Address successfully updated"));


//Retrieving newly updated Address
        String getPlaceResponse = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", "place_id")
                .when().get("/maps/api/place/get/json")
                .then()
                .assertThat()
                .log().all()
                .statusCode(200)
                .extract().response().asString();
        JsonPath jsA = new JsonPath(getPlaceResponse);
        String actualAddress = jsA.getString("address");
        System.out.println(actualAddress);
        Assert.assertEquals(actualAddress,newAddress);
    }
}