package api;


import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static api.ApiData.KeeperEndPoints;
import static api.ApiData.KeeperEndPoints.*;
import static api.ApiData.OrderData.R_KEEPER_RESTAURANT;
import static api.ApiData.QueryParams.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;

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
               // .log().ifError()
                .statusCode(200)
                .extract()
                .response();


        if (response.getStatusCode() != 200)
            await().atMost(10, TimeUnit.SECONDS).untilAsserted
                    (() -> Assertions.assertEquals(response.getStatusCode(), 200,
                            "Запрос не выполнился за определенный тауймаут"));

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
                    .post(KeeperEndPoints.fillingOrder)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract()
                    .response();

            Assertions.assertTrue(response.jsonPath().getBoolean("success"),
                    "Не удалось наполнить заказ: " + response.jsonPath().getString("result"));

            hasError = response.jsonPath().getString("result.Errors");

            if (hasError != null) {

                errorCounter++;

            } else {

                errorCounter = 3;

            }

        } while (errorCounter < 3);

    }

    @Step("Смена официанта")
    public boolean changeWaiter(Map<String, Object> rsBody, String baseUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .baseUri(baseUri)
                .body(rsBody)
                .when()
                .put(changeWaiter)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");



    }

    @Step("Получаем транзакцию по оплате в б2п")
    public Response getB2BPayment(String transaction_id) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get(b2bPaymentTransactionStatus + transaction_id)
                .then()
                //  .log().body()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertTrue(response.jsonPath().getBoolean("data.status"));
        Assertions.assertTrue(response.jsonPath().getBoolean("success"));

        return response;

    }

    @Step("Удаление позиции заказа")
    public boolean deletePosition(Map<String, Object> rsBody) {

           Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .baseUri(AUTO_API_URI)
                    .body(rsBody)
                    .when()
                    .delete(deletePosition)
                    .then()
                    .log().body()
                    .statusCode(200)
                    .extract()
                    .response();

            return response.path("message").equals("Операция прошла успешно");


    }

    @Step("Удаление позиции заказа")
    public boolean deletePosition(String requestBody, String baseUri) {

        Response response = given()
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

        return response.path("message").equals("Операция прошла успешно");


    }

    public void isDeletedDishPositions(String guid, String uni, int quantity) {

        await().pollInterval(2, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(() ->
                        Assertions.assertTrue(deletePosition(rqBodyDeletePosition(guid, uni, quantity))));

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
                    //.log().body()
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
               // .log().body()
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
    public boolean checkPrepayment(Map<String, Object> rsBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .baseUri(AUTO_API_URI)
                .when()
                .post(checkPrepayment)
                .then()
                //.log().body()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");

    }

    @Step("Проверка пришла ли предоплата")
    public Response checkPrepaymentOnlyKeeper(Map<String, Object> rsBody) {

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .baseUri(AUTO_API_URI)
                .when()
                .post(getPrepayment)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

    }

    @Step("Оплата заказа")
    public boolean orderPay(Map<String, Object> rsBody, String baseUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .baseUri(baseUri)
                .when()
                .post(orderPay)
                .then()
              //  .log().body()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");

    }

    @Step("Удаление администратора ресторана")
    public boolean deleteRestaurantAdmin(int id, String baseUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .baseUri(baseUri)
                .when()
                .delete(deleteRestaurantAdmin + "/" + id)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");

    }

    public int adminLogin(Map<String, Object> rsBody, String baseUri) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .baseUri(baseUri)
                .when()
                .post(adminLogin)
                .then()
                .extract()
                .response();

        return response.path("result.users_id") != null ?
                response.jsonPath().getInt("result.users_id") : 0;


    }

    @Step("Проверка что оплата прошла")
    public void isPaymentSuccess(String restaurantName, String guid, int paySum) {

        await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK, TimeUnit.MILLISECONDS)
                .timeout(Duration.ofMillis(WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK)).untilAsserted(() ->
                        Assertions.assertTrue(orderPay(rqBodyOrderPay(restaurantName,guid,paySum),AUTO_API_URI)));

    }

    @Step("Проверка что предоплата пришла")
    public void isPrepaymentSuccess(String transactionId, int maxTimeout) {

        await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(maxTimeout, TimeUnit.MILLISECONDS).timeout(Duration.ofMillis(maxTimeout)).untilAsserted(() ->
                        Assertions.assertTrue(checkPrepayment(rqBodyCheckPrePayment(transactionId)),
                                "Предоплата не пришла на кассу"));

    }

    @Step("Проверка что предоплата пришла на кипере")
    public void isPrepaymentSuccessOnlyKeeper(String guid, int maxTimeout) {

        await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(maxTimeout, TimeUnit.MILLISECONDS).timeout(Duration.ofMillis(maxTimeout)).untilAsserted(() ->
                        Assertions.assertTrue(checkPrepaymentOnlyKeeper(rqBodyHasPrepaymentOnlyKeeper(guid)).jsonPath()
                                        .getBoolean("has_prepayment"), "Предоплата не пришла на кассу"));

    }

    @Step("Закрываем заказ через апи")
    public void closedOrderByApi(String restaurantName, String tableId, String guid) {

        Response rs = apiRKeeper.getOrderInfo(tableId,AUTO_API_URI);

        int paySum = Integer.parseInt(apiRKeeper.getOrderSumFromGetOrder(rs));

        apiRKeeper.isPaymentSuccess(restaurantName,guid,paySum);

        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,AUTO_API_URI),
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
                    .log().body()
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
                .get("https://api.telegram.org/bot5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI/getUpdates?offset=-15")
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

                apiRKeeper.deleteEmptyOrder(apiRKeeper.rqBodyDeleteEmptyOrder(restaurantName,visit),apiUri);

            } else {

                apiRKeeper.isPaymentSuccess(restaurantName,guid,Integer.parseInt(orderSum));

            }


            isTableEmpty(restaurantName,tableId,apiUri);
            isSuccess = true;

        } else {


            String errorMessage = rs.jsonPath().getString("errors.error");

            if (errorMessage.contains("Не найдена информация по запросу.") ||
                    errorMessage.contains("Not Found") ) {

                isSuccess = true;

            } else {

                System.out.println("Ошибка: " + errorMessage);

            }

        }

        return isSuccess;

    }

    public void authorizeInPersonalAccount(String url,Map<String, Object> rsBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .when()
                .post(loginPersonalAccount)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        String userToken = response.jsonPath().getString("result.access_token");
        String refreshToken = response.jsonPath().getString("result.refresh_token");

        baseActions.openPage(url);

        Selenide.localStorage().setItem("userTokenData",userToken);
        Selenide.localStorage().setItem("refreshTokenData",refreshToken);

        baseActions.openPage(url);

    }

    public void authorizeInPersonalAccountByExpiredToken(String url,Map<String, Object> rsBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBody)
                .when()
                .post(loginPersonalAccount)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        String userToken = response.jsonPath().getString("result.access_token");
        String refreshToken = response.jsonPath().getString("result.refresh_token");

        baseActions.openPage(url);

        Selenide.localStorage().setItem("userTokenData",userToken);
        Selenide.localStorage().setItem("refreshTokenData",refreshToken);

        baseActions.openPage(url);

    }

    public void deleteAdmin(String email,String password) {

        int adminId = apiRKeeper.adminLogin(apiRKeeper.rqBodyAdminLogin(email, password),AUTO_API_URI);

        if (adminId != 0)
            apiRKeeper.deleteRestaurantAdmin(adminId,AUTO_API_URI);


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

    public Map<String, Object> rqBodyAdminLogin(String login, String password) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("email", login);
        rsBody.put("password", password);

        return rsBody;

    }

    public Map<String, Object> rqBodyChangeWaiter(String domen, String guid, String waiterId) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", domen);
        rsBody.put("guid", guid);
        rsBody.put("waiter_id", waiterId);

        return rsBody;

    }

    public Map<String, Object> rqBodyLoginPersonalAccount(String email, String password) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("email", email);
        rsBody.put("password", password);

        return rsBody;

    }

    public Map<String, Object> rqBodyCheckPrePayment(String transactionId) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("transaction_id", transactionId);

        return rsBody;

    }

    public Map<String, Object> rqBodyDeletePosition(String guid, String uni, int quantity) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", R_KEEPER_RESTAURANT);
        rsBody.put("guid", guid);
        rsBody.put("station", 1);
        rsBody.put("uni", uni);
        rsBody.put("quantity", quantity);

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

    public ArrayList<LinkedHashMap<String, Object>>
    createDishObject(ArrayList<LinkedHashMap<String, Object>> array, String dishId, double quantity) {

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
    rqBodyFillModificatorArrayWithDishes(String dishId, double quantity,
                                         ArrayList<LinkedHashMap<String, Object>> modifiers ) {

        LinkedHashMap<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("id", dishId);
        dishObject.put("quantity", quantity);
        dishObject.put("modificators", modifiers);


        return dishObject;

    }

    public Map<String, Object> rqBodyHasPrepaymentOnlyKeeper(String guid) {

        Map<String, Object> dishObject = new LinkedHashMap<>();

        dishObject.put("domen", "testrkeeper");
        dishObject.put("guid", guid);
        dishObject.put("station", 1);

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

}
