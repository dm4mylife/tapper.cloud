package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static api.EndPoints.*;
import static constants.Constant.TestData.API_URI;
import static io.restassured.RestAssured.given;

public class ApiRKeeper {

        public String createOrder() {

            String requestBody = "{\n" +
                    "  \"subDomen\": \"testrkeeper\",\n" +
                    "  \"tableCode\": 12,\n" +
                    "  \"waiterCode\": 20,\n" +
                    "  \"persistentComment\": 100500\n" +
                    "}";

            Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_URI)
                    .when()
                    .post(createOrder)
                    .then()
                    .statusCode(200)
                    .log().headers()
                    .extract()
                    .response();


            Assert.assertTrue(response.jsonPath().getBoolean("success"));


            String visit = response.jsonPath().getString("result.visit");
            System.out.println(visit);

            return visit;

        }



        public void fillingOrder(String visit) {


            String requestBody = "{\n" +
                    "  \"subDomen\": \"testrkeeper\",\n" +
                    "  \"quantity\": 1000,\n" +
                    "  \"visit\": \"" + visit + "\",\n" +
                    "  \"dishId\": \"1000303\"\n" +
                    "}";

            Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_URI)
                    .when()
                    .post(fillingOrder)
                    .then()
                    .statusCode(200)
                    .log().headers()
                    .extract()
                    .response();

            Assert.assertTrue(response.jsonPath().getBoolean("success"));
            Assert.assertNotEquals(null,response.jsonPath().getMap("result"));
            Assert.assertNull(response.jsonPath().getString("result.Error"));
            Assert.assertNull(response.jsonPath().getString("result.Errors"));
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
                    .baseUri(API_URI)
                    .when()
                    .post(deleteOrder)
                    .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .response();


            Assert.assertTrue(response.jsonPath().getBoolean("success"));
            Assert.assertEquals("Ok",response.jsonPath().getString("result.@attributes.Status"));


        }




}
