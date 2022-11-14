package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;


import static api.ApiData.*;
import static api.ApiData.EndPoints.orderGet;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.Constant.TestData.API_TEST_URI;
import static io.restassured.RestAssured.given;

public class ApiRKeeper {


    @Step("Получаем информацию по заказу")
    public Response getOrder(String requestBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(API_STAGE_URI)
                .when()
                .post(orderGet)
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTime() / 1000 + "sec response time");

        return response;
    }



        @Step("Создание заказа")
        public String createOrder(String requestBody) {


            Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_STAGE_URI)
                    .when()
                    .post(EndPoints.createOrder)
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
                    .post(EndPoints.fillingOrder)
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
                    .post(EndPoints.deleteOrder)
                    .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .response();


            Assertions.assertTrue(response.jsonPath().getBoolean("success"));
            Assertions.assertEquals("Ok",response.jsonPath().getString("result.@attributes.Status"));


        }




}
