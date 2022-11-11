package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;


import static api.EndPoints.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.Constant.TestData.API_TEST_URI;
import static io.restassured.RestAssured.given;

public class ApiRKeeper {



    /* RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000)
                    .setParam(CoreConnectionPNames.SO_TIMEOUT, 10000)); */

        @Step("Создание заказа")
        public String createOrder() {

            String requestBody = "{\n" +
                    "  \"subDomen\": \"testrkeeper\",\n" +
                    "  \"tableCode\": 12,\n" +
                    "  \"waiterCode\": 23,\n" +
                    "  \"persistentComment\": 100500\n" +
                    "}";

            Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_STAGE_URI)
                    .when()
                    .post(createOrder)
                    .then()
                    .log().status()
                    .statusCode(200)
                    .extract()
                    .response();

            System.out.println(response.getTime() / 1000 + "sec response time");

            Assertions.assertTrue(response.jsonPath().getBoolean("success"));

            return response.jsonPath().getString("result.visit");

        }


        @Step("Наполнение заказа")
        public void fillingOrder(String requestBody) {

            Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_STAGE_URI)
                    .when()
                    .post(fillingOrder)
                    .then()
                    .log().status()
                    .statusCode(200)
                    .extract()
                    .response();

            System.out.println(response.getTime() / 1000 + "sec response time");

            Assertions.assertTrue(response.jsonPath().getBoolean("success"));
            Assertions.assertNotEquals(null,response.jsonPath().getMap("result"));
            Assertions.assertNull(response.jsonPath().getString("result.Error"));
            Assertions.assertNull(response.jsonPath().getString("result.Errors"));
        }








        public void deleteOrderEmpty(String visitId) {

            String requestBody = "{\n" +
                    "  \"subDomen\": \"testrkeeper\",\n" +
                    "  \"visitId\": \"" + visitId + "\"\n" +
                    "}";

            Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_TEST_URI)
                    .when()
                    .post(deleteOrder)
                    .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .response();


            Assertions.assertTrue(response.jsonPath().getBoolean("success"));
            Assertions.assertEquals("Ok",response.jsonPath().getString("result.@attributes.Status"));


        }




}
