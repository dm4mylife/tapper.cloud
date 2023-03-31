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

    @Step("Создание заказа")
    public Response createOrder(LinkedHashMap<String, Object> rsBodyCreateOrder, String apiUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBodyCreateOrder)
                .baseUri(apiUri)
                .when()
                .post(createOrder)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        return response;

    }

    @Step("Наполнение заказа")
    public void fillingOrder(Map<String, Object> rsBody) {

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

                errorCounter = 3;

            }

            //System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        } while (errorCounter < 3);

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

        Assertions.assertTrue(response.jsonPath().getBoolean("data.status"));
        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        return response;

    }

    @Step("Удаление позиции заказа")
    public void deletePosition(String requestBody, String baseUri) {

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
                    .log().ifError()
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

    public void deleteEmptyOrder(Map<String, Object> rsBody, String baseUri) {

        boolean hasError;
        Response response;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(baseUri)
                    .body(rsBody)
                    .when()
                    .post(deleteOrder)
                    .then()
                    .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            //System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

            hasError = response.path("success").equals(true);

            if (!hasError) {

                errorCounter++;
                System.out.println("\nОшибка в запросе, будет сделан повторный запрос. Повторная попытка № "
                        + errorCounter + "\n");

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

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(AUTO_API_URI)
                    .body(rsBody)
                    .when()
                    .post(addDiscount)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract()
                    .response();

            hasError = response.path("message").equals("Операция прошла успешно");

            if (!hasError) {

                errorCounter++;
                System.out.println("Ошибка в запросе -> " + hasError);

            } else {

                errorCounter = 3;

            }

           // System.out.println("Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        } while (errorCounter < 3);

    }

    @Step("Удаление скидки заказа")
    public void deleteDiscount(Map<String, Object> rqBody, String baseUri) {

        boolean hasError;
        Response response;
        int errorCounter = 0;

        do {

            response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(baseUri)
                    .body(rqBody)
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
    public Response getOrderInfo(String tableId, String apiUri) {

        //System.out.println("Информация о заказе на кассе получена.Время исполнение запроса "+ response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

        return given()
                .contentType(ContentType.JSON)
                .and()
                .queryParam("table_id", tableId)
                .queryParam("domen", R_KEEPER_RESTAURANT)
                .baseUri(apiUri)
                .when()
                .get(getOrderInfo)
                .then()
              //  .log().ifError()
                .extract()
                .response();

    }

    @Step("Получаем uni")
    public String getUniFirstValueFromOrderInfo(String tableId, String apiUri) {

        Response rs = apiRKeeper.getOrderInfo(tableId,apiUri);

        String uniPath = "result.CommandResult.Order.Session.Dish[\"@attributes\"].uni";
        String uni = "";

        if (rs.path(uniPath) instanceof String) {

            uni = rs.jsonPath().getString(uniPath) + " string";

            return uni;

        } else if (rs.path(uniPath) instanceof List) {

            uni = rs.jsonPath().getList(uniPath).get(0).toString();

            return uni;

        } else if (rs.path(uniPath) == null) {

            uni = rs.jsonPath().getString("result.CommandResult.Order.Session.Dish[0][\"@attributes\"].uni");

        }

        return uni;

    }

    @Step("Проверка пришла ли предоплата")
    public Response checkPrepayment(String requestBody, String baseUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .baseUri(baseUri)
                .when()
                .post(checkPrepayment)
                .then()
                //.log().body()
                .statusCode(200)
                .extract()
                .response();

        return response;

    }

    @Step("Оплата заказа")
    public Response orderPay(Map<String, Object> rsBody, String baseUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .baseUri(baseUri)
                .when()
                .post(orderPay)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        return response;

        //System.out.println("Оплатили заказ.Время исполнение запроса " + response.getTimeIn(TimeUnit.SECONDS) + "сек\n");

    }

    @Step("Проверка что оплата прошла")
    public void isPaymentSuccess(String restaurantName, String guid, int paySum) {

        String hasErrorText = "";
        int rsCounter = ATTEMPT_FOR_PREPAYMENT_REQUEST;
        boolean rqResponse;

        do {

            baseActions.forceWait(Constants.WAIT_FOR_PREPAYMENT_ON_CASH_DESK);
            Response rs = orderPay(rqBodyOrderPay(restaurantName,guid,paySum),AUTO_API_URI);
            rqResponse = rs.jsonPath().getBoolean("success");
            hasErrorText = rs.jsonPath().getString("message");

            if (rqResponse) {
                break;
            }

            hasErrorText += "\nОплата не пришла даже после " + ATTEMPT_FOR_PREPAYMENT_REQUEST + "№ попытки";
            --rsCounter;

        } while (rsCounter != 0);

        Assertions.assertTrue(rqResponse, "Оплата не пришла" + hasErrorText);


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

            hasErrorText = " .Предоплата не пришла даже после " + ATTEMPT_FOR_PREPAYMENT_REQUEST + "№ попытки";
            --rsCounter;

        } while (rsCounter != 0);

        Assertions.assertTrue(rqResponse, "Предоплата не пришла" + hasErrorText);

    }

    @Step("Закрываем заказ через апи")
    public void closedOrderByApi(String restaurantName, String tableId, String guid, String apiUri) {

        Response rs = apiRKeeper.getOrderInfo(tableId,apiUri);

        int paySum = Integer.parseInt(apiRKeeper.getOrderSumFromGetOrder(rs));

        apiRKeeper.isPaymentSuccess(restaurantName,guid,paySum);

        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,apiUri),
                "На столе был прошлый заказ, его не удалось закрыть");

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
                .get("https://api.telegram.org/bot5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI/getUpdates?offset=-25")
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getList("result.channel_post.text");

    }

    @Step("Проверяем не закрыт ли стол")
    public boolean isTableEmpty(String restaurantName, String tableId, String apiUri) {

        Response rs = apiRKeeper.getOrderInfo(tableId,apiUri);

        boolean isSuccess = false;
        boolean hasError = rs.jsonPath().getBoolean("success");

        if (hasError) {

            String visit = apiRKeeper.getVisitFromGetOrder(rs);
            String guid = apiRKeeper.getGuidFromGetOrder(rs);
            String orderSum = apiRKeeper.getOrderSumFromGetOrder(rs);

            if (orderSum.equals("0")) {

                //System.out.println("Пустой заказ, закрываем пустой");
                apiRKeeper.deleteEmptyOrder(apiRKeeper.rqBodyDeleteEmptyOrder(restaurantName,visit),apiUri);

            } else {

                //System.out.println("Заказ имеет позиции, оплачиваем его и проверяем что закрыт");
                apiRKeeper.isPaymentSuccess(restaurantName,guid,Integer.parseInt(orderSum));

            }

            isTableEmpty(restaurantName,tableId,apiUri);
            isSuccess = true;

        } else {

            String errorMessage = rs.jsonPath().getString("errors.error");

            if (errorMessage.contains("Не найдена информация по запросу.") ||
                    errorMessage.contains("Not Found") ) {

                //System.out.println("На столе нет заказа");
                isSuccess = true;

            } else {

                System.out.println("Ошибка: " + errorMessage);

            }

        }

        return isSuccess;

    }

    public LinkedHashMap<String, Object>
    rqBodyCreateOrder(String restaurantName, String tableId, String waiterId) {

        LinkedHashMap<String, Object> createOrderDataMap = new LinkedHashMap<>();

        createOrderDataMap.put("subDomen", restaurantName);
        createOrderDataMap.put("tableCode", tableId);
        createOrderDataMap.put("waiterCode", waiterId);
        createOrderDataMap.put("persistentComment", "100500");

        return createOrderDataMap;

    }
    public Map<String, Object> rqBodyDeleteEmptyOrder(String domen, String visit) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("subDomen", domen);
        rsBody.put("visitId", visit);

        return rsBody;

    }
    public ArrayList<LinkedHashMap<String, Object>>
    createDishObject(ArrayList<LinkedHashMap<String, Object>> array, String dishId, int quantity) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", dishId);
        dishObject.put("quantity", quantity);

        array.add(dishObject);

        return array;

    }

    public Map<String, Object> rqBodyFillingOrder(String domen, String guid, ArrayList<LinkedHashMap<String,
            Object>> dishes) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("dishes", dishes);

        return rsBody;

    }

    public Map<String, Object> rqBodyAddDiscount(String domen, String guid, ArrayList<LinkedHashMap<String,
            Object>> discounts) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("discounts", discounts);

        return rsBody;

    }

    public void createDiscountByIdObject(ArrayList<LinkedHashMap<String, Object>> array, String discountId) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", discountId);

        array.add(dishObject);

    }
    public void createDiscountWithCustomSumObject(ArrayList<LinkedHashMap<String, Object>> array, String discountId,
                                                  String amount) {

        LinkedHashMap<String, Object> discountObject = new LinkedHashMap<>();

        discountObject.put("id", discountId);
        discountObject.put("amount", amount);

        array.add(discountObject);

    }
    public LinkedHashMap<String, Object>
    createModificatorObject(String dishId, int quantity) {

        LinkedHashMap<String, Object> modificatorObject = new LinkedHashMap<>();

        modificatorObject.put("id", dishId);
        modificatorObject.put("quantity", quantity);

        return modificatorObject;

    }
    public LinkedHashMap<String, Object>
    rqBodyFillModificatorArrayWithDishes(String dishId, int quantity,
                                         ArrayList<LinkedHashMap<String, Object>> modificators ) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", dishId);
        dishObject.put("quantity", quantity);
        dishObject.put("modificators", modificators);


        return dishObject;

    }
    public Map<String, Object> rqBodyAddModificatorOrder(String domen, String guid,
                                                         ArrayList<LinkedHashMap<String, Object>> dishes) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("dishes", dishes);

        return rsBody;

    }

    public LinkedHashMap<String, Object>
    rqBodyOrderPay(String restaurantName, String guid, int paySum) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("domen", restaurantName);
        dishObject.put("guid", guid);
        dishObject.put("station", 1);
        dishObject.put("pay", paySum);

        return dishObject;

    }

    public LinkedHashMap<String, Object>
    rqBodyDeleteDiscount(String restaurantName, String guid, String uni) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("domen", restaurantName);
        dishObject.put("guid", guid);
        dishObject.put("station", 1);
        dishObject.put("uni", uni);

        return dishObject;

    }

    public String getGuidFromCreateOrder(Response rs) {

        return rs.jsonPath().getString("result.guid");

    }
    public String getUniDiscountFromCreateOrder(String tableId,String apiUri) { // toDO сделать нормальный перебор по сессиям

        Response rs = getOrderInfo(tableId,apiUri);

        return rs.jsonPath().getString("result.CommandResult.Order.Session[1].Discount[\"@attributes\"].uni");

    }

    public String getVisitFromGetOrder(Response rs) {

        return rs.jsonPath().getString("result.CommandResult.Order[\"@attributes\"].visit");

    }

    public String getGuidFromGetOrder(Response rs) {

        return rs.jsonPath().getString("result.CommandResult.Order[\"@attributes\"].guid");

    }

    public String getOrderSumFromGetOrder(Response rs) {

        return rs.jsonPath().getString("result.CommandResult.Order[\"@attributes\"].orderSum");

    }

    public String getPrepaySumSumFromGetOrder(Response rs) {

        return rs.jsonPath().getString("result.CommandResult.Order[\"@attributes\"].prepaySum");

    }

}
