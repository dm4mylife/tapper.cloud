package api;


import common.BaseActions;
import data.Constants;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static api.ApiData.EndPoints;
import static api.ApiData.EndPoints.*;
import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static data.Constants.ATTEMPT_FOR_PREPAYMENT_REQUEST;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static io.restassured.RestAssured.given;

public class ApiRKeeper {

    BaseActions baseActions = new BaseActions();

    public LinkedHashMap<String, Object>
    rsCreateOrder(String restaurantName,String tableId, String waiterId) {

        LinkedHashMap<String, Object> createOrderDataMap = new LinkedHashMap<>();

        createOrderDataMap.put("subDomen", restaurantName);
        createOrderDataMap.put("tableCode", tableId);
        createOrderDataMap.put("waiterCode", waiterId);
        createOrderDataMap.put("persistentComment", "100500");

        return createOrderDataMap;

    }

    @Step("Создание заказа")
    public Response newCreateOrder(LinkedHashMap<String, Object> rsBodyCreateOrder, String apiUri, String tableId,
                                   String restaurant) {

          if (!newIsClosedOrder(restaurant,tableId,apiUri)) {

            System.out.println("На кассе есть прошлый заказ, закрываем его");
            String guid = getGuidFromOrderInfo(tableId,apiUri);
            System.out.println(guid + " guid");

            if (guid.equals("")) {

                System.out.println("На кассе пустой заказ на столе");

            } else {

                orderPay(rqParamsOrderPay(restaurant, guid), apiUri);

                boolean isOrderClosed = newIsClosedOrder(restaurant,tableId,apiUri);

                Assertions.assertTrue(isOrderClosed, "Заказ не закрылся на кассе");
                System.out.println("\nЗаказ закрылся на кассе\n");

            }

        }

        return newCreateOrderTest(rsBodyCreateOrder, apiUri);

    }


    @Step("Создаём заказ на кассе и наполняем его")
    public Response createAndFillOrder(String restaurantName, String tableCode, String waiterCode, String tableId,
                                       String apiUri,ArrayList<LinkedHashMap<String, Object>> rqBodyFillingOrder) {

        LinkedHashMap<String, Object> rqBodyCreateOrder =
                rsCreateOrder(restaurantName, tableCode, waiterCode);

        Response rsCreateOrder = newCreateOrder
                (rqBodyCreateOrder, apiUri, tableId, restaurantName);

        String guid = getGuidFromCreateOrder(rsCreateOrder);

        newFillingOrder(rsBodyFillingOrder(restaurantName, guid, rqBodyFillingOrder));

        return rsCreateOrder;

    }


    @Step("Создание заказа")
    public Response createOrder(String requestBody, String baseUri) {

      /*  if (!isClosedOrder()) {

            System.out.println("На кассе есть прошлый заказ, закрываем его");
            String guid = getGuidFromOrderInfo(TABLE_AUTO_111_ID,AUTO_API_URI);

            orderPay(rqParamsOrderPay(R_KEEPER_RESTAURANT, guid), AUTO_API_URI);

            boolean isOrderClosed = isClosedOrder();

            Assertions.assertTrue(isOrderClosed, "Заказ не закрылся на кассе");
            System.out.println("\nЗаказ закрылся на кассе\n");

        } */

        return createOrderTest(requestBody, baseUri);

    }

    public Response createOrderTest(String requestBody, String baseUri) {

        System.out.println("\nСоздание заказа\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(createOrder)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        System.out.println("Заказ создался на кассе.Время исполнение запроса "
                + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    public Response newCreateOrderTest(LinkedHashMap<String, Object> rqBody, String apiUri) {

        System.out.println("\nСоздание заказа\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rqBody)
                .baseUri(apiUri)
                .when()
                .post(createOrder)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        System.out.println("Заказ создался на кассе.Время исполнение запроса "
                + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Наполнение заказа")
    public Response fillingOrder(String requestBody) {

        System.out.println("\nНаполняем заказ\n");

        String hasError;
        Response response;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .and()
                    .body(requestBody)
                    .baseUri(AUTO_API_URI)
                    .when()
                    .post(EndPoints.fillingOrder)
                    .then()
                //    .log().body()
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

        } while (errorCounter < 3);

        return response;

    }

    @Step("Наполнение заказа")
    public Response newFillingOrder(Map<String, Object> rsBody) {

        System.out.println("\nНаполняем заказ\n");

        String hasError;
        Response response;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(rsBody)
                    .baseUri(AUTO_API_URI)
                    .when()
                    .post(EndPoints.fillingOrder)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract()
                    .response();

            Assertions.assertTrue(response.jsonPath().getBoolean("success"));

            hasError = response.jsonPath().getString("result.Errors");

            if (hasError != null) {

                errorCounter++;
                System.out.println("Ошибка запроса -> " + hasError);

            } else {

                System.out.println("Заказ наполнился позициями");
                errorCounter = 3;

            }

            System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        } while (errorCounter < 3);

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
                // .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

        Assertions.assertTrue(response.jsonPath().getBoolean("data.status"));
        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        System.out.println("Транзакция получена.Время исполнения запроса "
                + response.getTimeIn(TimeUnit.SECONDS) + " сек\n");
        return response;

    }

    @Step("Удаление позиции заказа")
    public void deletePosition(String requestBody, String baseUri) {

        System.out.println("\nУдаляем позицию с заказа\n" + requestBody);

        boolean hasError;
        Response response;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(baseUri)
                    .body(requestBody)
                    .when()
                    .delete(deletePosition)
                    .then()
                  //  .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

            hasError = response.path("message").equals("Операция прошла успешно");

            if (!hasError) {

                errorCounter++;
                System.out.println("\nОшибка в запросе, будет сделан повторный запрос. Попытка № " + errorCounter + "\n");

            } else {

                errorCounter = 3;

            }

        } while (errorCounter < 3);

    }

    @Step("Добавление скидки в заказ")
    public void createDiscount(Map<String, Object> rsBody) {

        boolean hasError;
        Response response;
        int errorCounter = 0;

        System.out.println(rsBody);

        System.out.println("\nДобавляем скидку в заказ\n");

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(AUTO_API_URI)
                    .body(rsBody)
                    .when()
                    .post(addDiscount)
                    .then()
                //    .log().body()
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

        } while (errorCounter < 3);

    }

    public Response deleteEmptyOrder(Map<String, Object> rsBody) {

        System.out.println("\nУдаляем пустой заказ\n" + rsBody);

        boolean hasError;
        Response response;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(AUTO_API_URI)
                    .body(rsBody)
                    .when()
                    .delete(deleteDiscount)
                    .then()
                    .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

            hasError = response.path("success").equals(true);

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

    @Step("Удаление скидки заказа")
    public void deleteDiscount(String requestBody, String baseUri) {

        System.out.println(requestBody);

        boolean hasError;
        Response response;
        int errorCounter = 0;

        do {

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
               // .log().ifError()
                .extract()
                .response();

        System.out.println("Информация о заказе на кассе получена.Время исполнение запроса "
                + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Получаем guid")
    public String getGuidFromCreateOrder(Response rs) {

        return rs.jsonPath().getString("result.guid");

    }

    @Step("Получаем uni")
    public String getUni(String tableId, String apiUri) {

        return apiRKeeper.getOrderInfo(tableId,apiUri).jsonPath().getString("Session.Dish['@attributes'].uni");

    }

    @Step("Получаем guid")
    public String getGuidFromOrderInfo(String tableId,String baseUri) {

        return getOrderInfo(tableId,baseUri).jsonPath().getString("@attributes.guid");

    }

    @Step("Получаем visit")
    public String geVisitFromCreateOrder(Response rs) {

        return rs.jsonPath().getString("result.guid");

    }

    @Step("Проверка закрыт ли текущий заказ на столе")
    public Response isOrderClosed(String requestBody, String baseUri) {

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

        System.out.println("Получили информацию по состоянию заказа.Время исполнение запроса "
                + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Проверка пришла ли предоплата")
    public Response checkPrepayment(String requestBody, String baseUri) {

        System.out.println("\nПолучаем информацию пришла ли предоплата\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(checkPrepayment)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Получили информацию по предоплате.Время исполнение запроса " +
                response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return response;

    }

    @Step("Оплата заказа")
    public void orderPay(String requestBody, String baseUri) {

        System.out.println("\nОплачиваем заказ\n");

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(orderPay)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Оплатили заказ.Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

    }

    @Step("Проверка что предоплата пришла")
    public void isPrepaymentSuccess(String transactionId) {

        String hasErrorText = "";
        int rsCounter = ATTEMPT_FOR_PREPAYMENT_REQUEST;
        boolean rqResponse;

        do {

            baseActions.forceWait(Constants.WAIT_FOR_PREPAYMENT_ON_CASH_DESK);
            Response rs = checkPrepayment(rqParamsCheckPrePayment(transactionId), AUTO_API_URI);
            rqResponse = rs.jsonPath().getString("message").equals("Предоплата прошла по кассе");

            if (rqResponse) {
                break;
            }

            System.out.println("Предоплата не пришла, делаем повторный запрос");
            hasErrorText = " .Предоплата не пришла даже после " + ATTEMPT_FOR_PREPAYMENT_REQUEST + "№ попытки";
            --rsCounter;

        } while (rsCounter != 0);

        Assertions.assertTrue(rqResponse, "Предоплата не пришла" + hasErrorText);

    }

    @Step("Проверка что заказ закрыт на столе")
    public boolean isClosedOrder(String restaurantName, String tableId, String apiUri) {

        Response rsGetOrder = getOrderInfo(tableId, apiUri);
        Response isOrderClosed = null;
        String guid;
        boolean isClosed = false;

        if (rsGetOrder.statusCode() == 500) {

            System.out.println("Пустой стол");
            return true;

        } else if (rsGetOrder.statusCode() != 200) {

            System.out.println("Ошибка. Запрос не исполнился");

        } else {

            guid = getGuidFromCreateOrder(rsGetOrder);
            isOrderClosed = isOrderClosed(rqParamsIsOrderClosed(restaurantName, guid), apiUri);

        }

        if (isOrderClosed != null) {

            if (isOrderClosed.jsonPath().getString("message").equals("Заказ НЕ закрыт на кассе")) {

                System.out.println("Заказ НЕ закрыт или его нет на кассе");

            }

        } else {

            boolean isSuccessRs = isOrderClosed.jsonPath().getBoolean("success");

            System.out.println(isOrderClosed.jsonPath().getBoolean("message"));

            if (isSuccessRs) {

                isClosed = true;

            }

        }

        return isClosed;

    }

    @Step("Проверка что заказ закрыт на столе")
    public boolean newIsClosedOrder(String restaurantName, String tableId, String apiUri) {

        Response rsGetOrder = getOrderInfo(tableId, apiUri);
        Response isOrderClosed = null;
        String guid;
        boolean isClosed = false;

        if (rsGetOrder.statusCode() == 500) {

            System.out.println("Пустой стол");
            return true;

        } else if (rsGetOrder.statusCode() != 200) {

            System.out.println("Ошибка. Запрос не исполнился");

        } else {

            guid = getGuidFromCreateOrder(rsGetOrder);
            isOrderClosed = isOrderClosed(rqParamsIsOrderClosed(restaurantName, guid), apiUri);

        }

        if (isOrderClosed != null) {

            if (isOrderClosed.jsonPath().getString("message").equals("Заказ НЕ закрыт на кассе")) {

                System.out.println("Заказ НЕ закрыт или его нет на кассе");

            }

        } else {

            boolean isSuccessRs = isOrderClosed.jsonPath().getBoolean("success");

            System.out.println(isOrderClosed.jsonPath().getBoolean("message"));

            if (isSuccessRs) {

                isClosed = true;

            }

        }

        return isClosed;

    }

    @Step("Добавление модификатора в заказ")
    public Response addModificatorOrder(Map<String, Object> rsBody) {

        Response response;
        boolean hasError;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(rsBody)
                    .baseUri(AUTO_API_URI)
                    .when()
                    .post(addModificatorOrder)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract()
                    .response();

            hasError = response.path("message").equals("Операция прошла успешно");

            System.out.println(response.jsonPath().getString("message"));

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

    @Step("Получение сообщений из бота в канале")
    public List<Object> getUpdates() {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get("https://api.telegram.org/bot5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI/getUpdates?offset=-100")
                .then()
                //.log().body()
                .statusCode(200)
                .extract()
                .response();

        List<Object> tgMessages = response.jsonPath().getList("result.channel_post.text");
        System.out.println("Количество сообщений " + tgMessages.size());

        return tgMessages;

    }

    public Map<String, Object> rsBodyFillingOrder(String domen, String guid, ArrayList<LinkedHashMap<String, Object>> dishes) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("dishes", dishes);

        return rsBody;

    }

    public Map<String, Object> rsBodyAddDiscount(String domen, String guid, ArrayList<LinkedHashMap<String, Object>> discounts) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("discounts", discounts);

        return rsBody;

    }

    public Map<String, Object> rsBodyDeleteEmptyOrder(String visit) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("subDomen", "testrkeeper");
        rsBody.put("visitId", visit);

        return rsBody;

    }

    public ArrayList<LinkedHashMap<String, Object>>
    orderFill(ArrayList<LinkedHashMap<String, Object>> array, String dishId, int quantity) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", dishId);
        dishObject.put("quantity", quantity);

        array.add(dishObject);

        return array;

    }

    public ArrayList<LinkedHashMap<String, Object>>
    createDiscountWithCustomSum(ArrayList<LinkedHashMap<String, Object>> array, String discountId, String amount) {

        LinkedHashMap<String, Object> discountObject = new LinkedHashMap<>();

        discountObject.put("id", discountId);
        discountObject.put("amount", amount);

        array.add(discountObject);

        return array;

    }

    public ArrayList<LinkedHashMap<String, Object>>
    createDiscountById(ArrayList<LinkedHashMap<String, Object>> array, String discountId) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", discountId);

        array.add(dishObject);

        return array;

    }

    public Map<String, Object> rsBodyAddModificatorOrder(String domen, String guid,
                                                         ArrayList<LinkedHashMap<String, Object>> dishes) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("dishes", dishes);

        return rsBody;

    }

    public LinkedHashMap<String, Object>
    fillModificatorArrayWithDishes(String dishId, int quantity,
                                   ArrayList<LinkedHashMap<String, Object>> modificators ) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", dishId);
        dishObject.put("quantity", quantity);
        dishObject.put("modificators", modificators);


        return dishObject;

    }

    public LinkedHashMap<String, Object>
    createModificatorObject(String dishId, int quantity) {

        LinkedHashMap<String, Object> modificatorObject = new LinkedHashMap<>();

        modificatorObject.put("id", dishId);
        modificatorObject.put("quantity", quantity);

        return modificatorObject;

    }


}
