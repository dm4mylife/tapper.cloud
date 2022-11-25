package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.TimeUnit;

import static api.ApiData.EndPoints;
import static api.ApiData.EndPoints.*;
import static api.ApiData.orderData.R_KEEPER_RESTAURANT;
import static constants.Constant.TestData.API_STAGE_URI;
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
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");
        System.out.println("\nИнформация получена по заказу из кассы");

        return response;
    }

    @Step("Создание заказа")
    public Response createOrder(String requestBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(API_STAGE_URI)
                .when()
                .post(EndPoints.createOrder)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");
        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        System.out.println("\nЗаказ создался на кассе");
        return response;

    }

    @Step("Наполнение заказа")
    public Response fillingOrder(String requestBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(API_STAGE_URI)
                .when()
                .post(EndPoints.fillingOrder)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertNotEquals(null, response.jsonPath().getMap("result"));
        Assertions.assertNull(response.jsonPath().getString("result.Error"));
        Assertions.assertNull(response.jsonPath().getString("result.Errors"));

        System.out.println("\nЗаказ наполнился");

        return response;

    }

    @Step("Получаем транзакцию по оплате в б2п")
    public Response getB2BPayment(String transaction_id) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get(b2bPaymentTransactionStatus + transaction_id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("data.status"));
        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        System.out.println("Транзакция получена");
        return response;

    }

    @Step("Удаляем весь заказ если не пустой")
    public Response deleteOrder(String visit) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .queryParam("subDomen", R_KEEPER_RESTAURANT)
                .queryParam("visitId", visit)
                .baseUri(API_STAGE_URI)
                .post(deleteOrder)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertEquals(response.jsonPath().getString("result['@attributes'].Status"), "Ok");

        System.out.println("Транзакция удалена со стола");
        return response;

    }

    @Step("Удаление позиции заказа")
    public Response deletePosition(String guid, String uni, String quantity) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("domen", R_KEEPER_RESTAURANT)
                .queryParam("guid", guid)
                .queryParam("station", "1")
                .queryParam("item_code", uni)
                .queryParam("quantity", quantity)
                .when()
                .post("https://apitapper.zedform.ru/api/rkeeper-automation/delete-position")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertEquals(response.jsonPath().getString("message"), "Операция прошла успешно");

        System.out.println("\nУдалили");

        return response;

    }

    @Step("Закрываем заказ")
    public Response payOrder(String guid, String pay) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("subDomen", R_KEEPER_RESTAURANT)
                .queryParam("orderId", guid)
                .queryParam("pay", pay)
                .when()
                .post("https://apitapper.zedform.ru/api/rkeeper/payordertmp")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertEquals(response.jsonPath().getString("message"), "Операция прошла успешно");

        System.out.println("\nОплатили заказ, закрыли на кассе");

        return response;

    }

    @Step("Добавление модификатора в заказ")
    public Response addModificatorOrder(String guid, String dishId, String quantity, String modificator, String modificator_quantity) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("domen", R_KEEPER_RESTAURANT)
                .queryParam("guid", guid)
                .queryParam("station", "1")
                .queryParam("dish", dishId)
                .queryParam("quantity", quantity)
                .queryParam("modificator", modificator)
                .queryParam("modificator_quantity", modificator_quantity)
                .when()
                .post("https://apitapper.zedform.ru/api/rkeeper-automation/add-modificator-order")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertEquals(response.jsonPath().getString("message"), "Операция прошла успешно");

        System.out.println("\nДобавили модификатор для заказа");

        return response;

    }

    @Step("Добавление наценки в заказ")
    public Response addMarginOrder(String guid, String dishId, String quantity, String modificator, String modificator_quantity) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("domen", R_KEEPER_RESTAURANT)
                .queryParam("guid", guid)
                .queryParam("station", "1")
                .queryParam("dish", dishId)
                .queryParam("quantity", quantity)
                .queryParam("modificator", modificator)
                .queryParam("modificator_quantity", modificator_quantity)
                .when()
                .post("https://apitapper.zedform.ru/api/rkeeper-automation/add-modificator-order")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertEquals(response.jsonPath().getString("message"), "Операция прошла успешно");

        System.out.println("\nДобавили модификатор для заказа");

        return response;

    }

    @Step("Получение информации о заказе на столе")
    public Response getOrderInfo(String id_table) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("id_table", id_table)
                .queryParam("subDomen", R_KEEPER_RESTAURANT)
                .when()
                .post("https://apitapper.zedform.ru/api/rkeeper/order")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");
        System.out.println("\nПолучили информацию по заказу");

        return response;

    }

}
