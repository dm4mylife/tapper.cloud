package api;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tapper_table.RootPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static api.ApiData.EndPoints;
import static api.ApiData.EndPoints.*;
import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.emptyOrderHeading;
import static io.restassured.RestAssured.given;

public class ApiRKeeper {

    public Response responseErrorHandler(Response response,int successfulResponseAttempt) {

        String hasError;
        Response rs;
        int errorCounter = 0;

        do  {

            rs = response;

            hasError = rs.jsonPath().getString("result.Error");

            if (hasError != null) {

                System.out.println(hasError + " ошибка запроса");
                System.out.println("\nОшибка в запросе, будет сделан повторный запрос. Повторная попытка № "
                        + errorCounter + "\n");

                errorCounter++;

            } else {

                errorCounter = successfulResponseAttempt;

            }


        } while (errorCounter < successfulResponseAttempt);

        return rs;


    }

    @Step("Создание заказа")
    public Response createOrder(String requestBody, String baseUri) {

        if (!isClosedOrder()) {

            System.out.println("На кассе есть прошлый заказ, закрываем его");
            Response rsGetOrder = getOrderInfo(TABLE_3_ID,API_STAGE_URI);
            String guid = rsGetOrder.jsonPath().getString("@attributes.guid");

            boolean isOrderClosed;
            orderPay(rqParamsOrderPay(R_KEEPER_RESTAURANT,guid),API_STAGE_URI);

            isOrderClosed = isClosedOrder();

            Assertions.assertTrue(isOrderClosed,"Заказ не закрылся на кассе");
            System.out.println("\nЗаказ закрылся на кассе\n");

        }

        System.out.println("\nСоздание заказа\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(createOrder)
                .then()
                //.log().body()
                .statusCode(200)
                .extract()
                .response();


        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        System.out.println("Заказ создался на кассе");
        System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Наполнение заказа")
    public Response fillingOrder(String requestBody) {

        System.out.println("\nНаполняем заказ\n");

        String hasError;
        Response response;
        int errorCounter = 0;

        // do  {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(requestBody)
                    .baseUri(API_STAGE_URI)
                    .when()
                    .post(EndPoints.fillingOrder)
                    .then()
                  //  .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            Assertions.assertTrue(response.jsonPath().getBoolean("success"));
            Assertions.assertNotEquals(null, response.jsonPath().getMap("result"));

            hasError = response.jsonPath().getString("result.Errors");

            if (hasError != null) {

                errorCounter++;
                System.out.println("Ошибка запроса -> " + hasError);

            } else {

                System.out.println("Заказ наполнился позициями");
                errorCounter = 3;

            }


        System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

      //  } while (errorCounter < 3);



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
                //.log().body()
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
    public Response deletePosition(String requestBody,String baseUri) {

        System.out.println(requestBody);

        boolean hasError;
        Response response;

        do  {

        response = given()
                .contentType(ContentType.JSON)
                .and()
                .baseUri(baseUri)
                .body(requestBody)
                .when()
                .delete(deletePosition)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        hasError = !response.path("message").equals("Операция прошла успешно");

        if (hasError)
            System.out.println("\nОшибка в запросе, будет сделан повторный запрос\n");


    } while (hasError);

        System.out.println("\nУдалили позицию");
        return response;

    }

    @Step("Добавление скидки в заказ")
    public Response addDiscount(String requestBody,String baseUri) {

        boolean hasError;
        Response response;
        int errorCounter = 0;

        System.out.println("\nДобавляем скидку в заказ\n");

       // do  {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(baseUri)
                    .body(requestBody)
                    .when()
                    .post(addDiscount)
                    .then()
                    .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            hasError = response.path("message").equals("Операция прошла успешно");

            if (!hasError) {

                errorCounter++;
                System.out.println("Ошибка в запросе -> " + hasError);

            } else {

                System.out.println("Скидка добавилась к заказу");
                errorCounter = 3;

            }

        System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        //} while (errorCounter < 3);

        return response;

    }

    @Step("Удаление скидки заказа")
    public Response deleteDiscount(String requestBody,String baseUri) {

        System.out.println(requestBody);

        boolean hasError;
        Response response;
        int errorCounter = 0;

        do  {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(baseUri)
                    .body(requestBody)
                    .when()
                    .delete(deleteDiscount)
                    .then()
                    .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

            hasError = response.path("message").equals("Операция прошла успешно");

            if (!hasError) {

                errorCounter++;
                System.out.println("\nОшибка в запросе, будет сделан повторный запрос. Повторная попытка № "
                        + errorCounter + "\n");

            } else {

                errorCounter = 3;

            }

        } while (errorCounter < 3);

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

    @Step("Добавление наценки в заказ")
    public Response addMarginOrder(String guid, String dishId, String quantity,
                                   String modificator, String modificator_quantity) {

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
    public Response getOrderInfo(String id_table, String baseUri) {

        System.out.println("\nЗапрашиваем информацию о заказе на кассе\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("id_table", id_table)
                .queryParam("subDomen", R_KEEPER_RESTAURANT)
                .baseUri(baseUri)
                .when()
                .post(getOrderInfo)
                .then()
                //.log().ifError()
                .extract()
                .response();

        System.out.println("Информация о заказе на кассе");
        System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Проверка закрыт ли текущий заказ на столе")
    public Response isOrderClosed(String requestBody,String baseUri) {

        System.out.println("\nПолучаем информацию по состоянию заказа\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(checkOrderClosed)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();


        System.out.println("Получили информацию по состоянию заказа");
        System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Оплата заказа")
    public Response orderPay(String requestBody,String baseUri) {

        boolean hasError = false;
        System.out.println("\nОплачиваем заказ\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(orderPay)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        hasError = !response.path("message").equals("Операция прошла успешно");

        if (hasError)
            System.out.println("Ошибка в запросе -> " + hasError);

        System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Проверка что заказ закрыт на столе")
    public boolean isClosedOrder() {

        Response rsGetOrder = getOrderInfo(TABLE_3_ID,API_STAGE_URI);
        Response isOrderClosed = null;
        String guid;
        boolean isClosed = false;

        if (rsGetOrder.statusCode() == 500) {

            System.out.println("Пустой стол");
            return true;

        } else if (rsGetOrder.statusCode() != 200) {

            System.out.println("Ошибка. Запрос не исполнился");

        } else {

            guid = rsGetOrder.jsonPath().getString("@attributes.guid");
            System.out.println(guid + " guid");
            isOrderClosed = isOrderClosed(rqParamsIsOrderClosed(R_KEEPER_RESTAURANT, guid), API_STAGE_URI);

        }

        if (isOrderClosed != null) {

            if (isOrderClosed.jsonPath().getString("message").equals("Заказ НЕ закрыт на кассе")) {

                System.out.println("Заказ НЕ закрыт или его нет на кассе");

            }

        } else {

            System.out.println(isOrderClosed.jsonPath().getString("message"));
            isClosed = true;

        }

        return isClosed;

    }

    @Step("Добавление модификатора в заказ")
    public Response addModificatorOrder(String requestBody,
                                        String baseUri) {

        Response response;
        boolean hasError;

        do  {

        response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(addModificatorOrder)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        hasError = !response.path("message").equals("Операция прошла успешно");

        if (hasError)
            System.out.println("\nОшибка в запросе, будет сделан повторный запрос\n");

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

    } while (hasError);

        System.out.println("\nДобавили модификатор для заказа");
        return response;

    }

    @Test
    @Step("Создание заказа со всеми типами модификаторов")
    public void fillOrderWithAllModiDishes(String guid, String baseUri) {

        addModificatorOrder(
            rqParamsAddModificatorWith1Position(
                    R_KEEPER_RESTAURANT,guid, BORSH, "1000",
                    FREE_NECESSARY_MODI_SALT, "1")
            ,baseUri );

          addModificatorOrder(
                rqParamsAddModificatorWith1Position(
                        R_KEEPER_RESTAURANT,guid, BORSH, "1000",
                        FREE_NECESSARY_MODI_PEPPER, "2")
                ,baseUri );

        addModificatorOrder(
                rqParamsAddModificatorWith1Position(
                        R_KEEPER_RESTAURANT,guid, BORSH, "2000",
                        FREE_NECESSARY_MODI_PEPPER, "1")
                ,baseUri );

        addModificatorOrder(
                rqParamsAddModificatorWith2Positions(
                        R_KEEPER_RESTAURANT,guid, BORSH, "2000",
                        FREE_NECESSARY_MODI_PEPPER, "1",
                        FREE_NECESSARY_MODI_SALT,"1")
                ,baseUri );

      /*  addModificatorOrder(
                rqParamsAddModificatorWith2Positions(
                        R_KEEPER_RESTAURANT,guid, BORSH, "2000",
                        FREE_NECESSARY_MODI_PEPPER, "2",
                        FREE_NECESSARY_MODI_SALT,"2")
                ,baseUri );

        addModificatorOrder(
                rqParamsAddModificatorWith2Positions(
                        R_KEEPER_RESTAURANT,guid, BORSH, "1000",
                        FREE_NECESSARY_MODI_PEPPER, "1",
                        FREE_NECESSARY_MODI_SALT,"2")
                ,baseUri );

*/



 /*
        addModificatorOrder(guid, XOLODEC, "1000", FREE_NON_NECESSARY_MODI_BUTTER, "1", API_TEST_URI);
         addModificatorOrder(guid, XOLODEC, "1000", FREE_NON_NECESSARY_MODI_BUTTER, "2",API_TEST_URI);
        addModificatorOrder(guid, XOLODEC, "2000", FREE_NON_NECESSARY_MODI_BUTTER, "1",API_TEST_URI );

        addModificatorOrder(guid, XOLODEC, "1000", FREE_NON_NECESSARY_MODI_MAYONES, "1",API_TEST_URI );
          addModificatorOrder(guid, XOLODEC, "1000", FREE_NON_NECESSARY_MODI_MAYONES, "2",API_TEST_URI);
        addModificatorOrder(guid, XOLODEC, "2000", FREE_NON_NECESSARY_MODI_MAYONES, "1", API_TEST_URI);

        addModificatorOrder(guid, CAESAR, "1000", PAID_NECESSARY_MODI_BANAN_SIROP, "1", API_TEST_URI);
        addModificatorOrder(guid, CAESAR, "1000", PAID_NECESSARY_MODI_BANAN_SIROP, "2",API_TEST_URI );
        addModificatorOrder(guid, CAESAR, "2000", PAID_NECESSARY_MODI_BANAN_SIROP, "1",API_TEST_URI );

        addModificatorOrder(guid, CAESAR, "1000", PAID_NECESSARY_MODI_KARAMEL_SIROP, "1", API_TEST_URI);
        addModificatorOrder(guid, CAESAR, "1000", PAID_NECESSARY_MODI_KARAMEL_SIROP, "2", API_TEST_URI);
        addModificatorOrder(guid, CAESAR, "2000", PAID_NECESSARY_MODI_KARAMEL_SIROP, "1",API_TEST_URI );

        addModificatorOrder(guid, RAGU, "1000", PAID_NON_NECESSARY_MODI_SOUS, "1",API_TEST_URI );
           addModificatorOrder(guid, RAGU, "1000", PAID_NON_NECESSARY_MODI_SOUS, "2",API_TEST_URI);
        addModificatorOrder(guid, RAGU, "2000", PAID_NON_NECESSARY_MODI_SOUS, "1",API_TEST_URI );

        addModificatorOrder(guid, RAGU, "1000", PAID_NON_NECESSARY_MODI_SALAT, "1",API_TEST_URI );
         addModificatorOrder(guid, RAGU, "1000", PAID_NON_NECESSARY_MODI_SALAT, "2",API_TEST_URI);
        addModificatorOrder(guid, RAGU, "2000", PAID_NON_NECESSARY_MODI_SALAT, "1",API_TEST_URI );

        addModificatorOrder(guid, VODKA, "1000", PAID_NON_NECESSARY_MIX_MODI_SALO, "1",API_TEST_URI );
           addModificatorOrder(guid, VODKA, "1000", PAID_NON_NECESSARY_MIX_MODI_SALO, "2",API_TEST_URI);
        addModificatorOrder(guid, VODKA, "2000", PAID_NON_NECESSARY_MIX_MODI_SALO, "1",API_TEST_URI );

        addModificatorOrder(guid, VODKA, "1000", PAID_NON_NECESSARY_MIX_MODI_BREAD, "1", API_TEST_URI);
         addModificatorOrder(guid, VODKA, "1000", PAID_NON_NECESSARY_MIX_MODI_BREAD, "2",API_TEST_URI);
        addModificatorOrder(guid, VODKA, "2000", PAID_NON_NECESSARY_MIX_MODI_BREAD, "1",API_TEST_URI );

        addModificatorOrder(guid, PASTA, "1000", FREE_NECESSARY_MODI_SOUS, "1",API_TEST_URI );
          addModificatorOrder(guid, PASTA, "1000", FREE_NECESSARY_MODI_SOUS, "2",API_TEST_URI);
        addModificatorOrder(guid, PASTA, "2000", FREE_NECESSARY_MODI_SOUS, "1",API_TEST_URI );

        addModificatorOrder(guid, PASTA, "1000", PAID_NECESSARY_MODI_BACON, "1",API_TEST_URI );
           addModificatorOrder(guid, PASTA, "1000", PAID_NECESSARY_MODI_BACON, "2",API_TEST_URI);
        addModificatorOrder(guid, PASTA, "2000", PAID_NECESSARY_MODI_BACON, "1",API_TEST_URI ); */


    }

    @Test
    @Step("Получение сообщений из бота в канале")
    public List<Object> getUpdates() {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get("https://api.telegram.org/bot5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI/getUpdates?offset=-50")
                .then()
                //.log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        List<Object> tgMessages = response.jsonPath().getList("result.channel_post.text");

        return tgMessages;

    }

}
