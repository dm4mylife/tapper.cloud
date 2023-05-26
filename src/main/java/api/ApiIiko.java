package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static api.ApiData.IikoData.Dish.*;
import static api.ApiData.IikoData.IikoEndpoints.*;
import static api.ApiData.OrderData.Iiko_RESTAURANT;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static io.restassured.RestAssured.given;

public class ApiIiko {

    public LinkedHashMap<String, Object>
    rqBodyCreateOrder (String tableId) {

        LinkedHashMap<String, Object> createOrderDataMap = new LinkedHashMap<>();

        createOrderDataMap.put("domen", Iiko_RESTAURANT);
        createOrderDataMap.put("table_id", tableId);
        createOrderDataMap.put("comment", "test");

        return createOrderDataMap;

    }

    @Step("Создание заказа")
    public String createOrder(LinkedHashMap<String, Object> rsBodyCreateOrder) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .body(rsBodyCreateOrder)
                .baseUri(AUTO_API_URI)
                .when()
                .post(createOrder)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertTrue(response.jsonPath().getBoolean("success"));
        Assertions.assertNull(response.jsonPath().getJsonObject("error"));

        return response.jsonPath().getString("data");

    }


    public Map<String, Object> rqBodyFillingOrder(String order_id, String product_id, int amount) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", Iiko_RESTAURANT);
        rsBody.put("order_id", order_id);
        rsBody.put("product_id", product_id);
        rsBody.put("amount", amount);

        return rsBody;

    }


    public Map<String, Object> rqBodyFillingOrderWithModifiers(String order_id, String product_id, int amount,
                                                  ArrayList<LinkedHashMap<String, Object>> modifiers) {

        Map<String, Object> rsBody = rqBodyFillingOrder(order_id,product_id,amount);

        rsBody.put("modifiers", modifiers);

        return rsBody;

    }

    public ArrayList<LinkedHashMap<String, Object>>
    createModificatorObject(ArrayList<LinkedHashMap<String, Object>> modifiersArray, String modifiersId, int quantity) {

        LinkedHashMap<String, Object> modificatorObject = new LinkedHashMap<>();

        modificatorObject.put("Key", modifiersId);
        modificatorObject.put("Value", quantity);

        modifiersArray.add(modificatorObject);

        return modifiersArray;

    }

    @Step("Наполнение заказа")
    public void fillingOrder(Map<String, Object> rsBody) {

           Response response = given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(rsBody)
                    .baseUri(AUTO_API_URI)
                    .when()
                    .put(fillingOrder)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract()
                    .response();

            Assertions.assertTrue(response.jsonPath().getBoolean("success"));

    }


    @Step("Получение информации о заказе на столе")
    public Response getOrderInfo(String tableId) {

        Map<String,Object> map = new HashMap<>();
            map.put("table_id",tableId);
            map.put("domen",Iiko_RESTAURANT);

        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(map)
                .baseUri(AUTO_API_URI)
                .when()
                .post(getOrderInfo)
                .then()
                .log().ifError()
                .extract()
                .response();

    }

    public Map<String, Object> rqBodyAddDiscount(String orderId, String discountId, int amount) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", Iiko_RESTAURANT);
        rsBody.put("order_id", orderId);
        rsBody.put("discount", discountId);
        rsBody.put("sum", amount);

        return rsBody;

    }

    @Step("Добавление скидки в заказ")
    public boolean createDiscount(Map<String, Object> rsBody) {

        Response response = given()
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

        return response.jsonPath().getBoolean("success");

    }

    @Step("Добавление скидки в заказ")
    public boolean deleteDiscount(Map<String, Object> rsBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .baseUri(AUTO_API_URI)
                .body(rsBody)
                .when()
                .delete(deleteDiscount)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");

    }


    public Map<String, Object> rqBodyDeletePosition(String orderId, String positionId, String productId) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", Iiko_RESTAURANT);
        rsBody.put("order_id", orderId);
        rsBody.put("position_id", positionId);
        rsBody.put("product_id", productId);
        return rsBody;

    }

    public Map<String, Object> rqBodyDeleteDiscount(String orderId, String discountId) {

        Map<String, Object> rsBody = new LinkedHashMap<>();
        rsBody.put("domen", Iiko_RESTAURANT);
        rsBody.put("order_id", orderId);
        rsBody.put("discount", discountId);
        return rsBody;

    }

    @Step("Удаление заказа")
    public boolean deleteOrder(String orderId) {

        Map<String,Object> map = new HashMap<>();
        map.put("domen",Iiko_RESTAURANT);
        map.put("order_id",orderId);

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .baseUri(AUTO_API_URI)
                .body(map)
                .when()
                .delete(deleteOrder)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");


    }

    @Step("Удаление позиции заказа")
    public boolean deletePosition(Map<String, Object> requestBody) {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .baseUri(AUTO_API_URI)
                .body(requestBody)
                .when()
                .delete(deletePosition)
                .then()
                .log().ifError()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getBoolean("success");


    }

    public double getPaidSum(Response response) {

        List<Map<String, Object>> orders = response.jsonPath().getList("result.ordersElementAll");
        double totalSum = 0;

        for (Map<String, Object> order : orders) {
            if (order.get("status").equals("PAID"))
                totalSum +=  (int) order.get("finalPrice");
        }

        return totalSum / 100;

    }

    public double getUnpaidSum(Response response) {

        List<Map<String, Object>> orders = response.jsonPath().getList("result.ordersElementAll");
        double totalSum = 0;

        for (Map<String, Object> order : orders) {
            if (order.get("status").equals("NOT_PAID"))
                totalSum +=  (int) order.get("finalPrice");
        }

        return totalSum /= 100;

    }

    public double getDishesPositionsId(Response response) {

        List<Map<String, Object>> orders = response.jsonPath().getList("result.ordersElementAll");
        double totalSum = 0;

        for (Map<String, Object> order : orders) {
            if (order.get("status").equals("NOT_PAID"))
                totalSum +=  (int) order.get("finalPrice");
        }

        return totalSum /= 100;

    }

    public void getDishesNameAndPositions(Response response) {

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> ordersElementAll = jsonPath.getList("result.ordersElementAll");
        String orderId = response.jsonPath().getString("result.orders[0].order_id");

        HashMap<String, String> namePositionMap = new HashMap<>();

        for (Map<String, Object> element : ordersElementAll) {

            namePositionMap.put(
                    element.get("name").toString(),
                    element.get("position_id").toString());

            replaceNameWithDishIdEnum(orderId,namePositionMap);

        }

    }

    public void replaceNameWithDishIdEnum(String orderId,HashMap<String, String> dishMap) {

        for (Map.Entry<String, String> entry : dishMap.entrySet()) {
            String dishName = entry.getKey();
            String originalValue = entry.getValue();

            for (ApiData.IikoData.Dish dish : ApiData.IikoData.Dish.values()) {
                if (dish.getName().equals(dishName)) {

                    deletePosition(rqBodyDeletePosition(orderId,originalValue,dish.getId()));

                    break;

                }
            }
        }

    }

    public String isClosedOrder(String tableId) {

        return getOrderInfo(tableId).jsonPath().getString("result.ordersElementAll");

    }

    public boolean deletePositionsAndOrder(String tableId) {

        Response getOrder = getOrderInfo(tableId);

        if (getOrder.path("result.orders[0].order_id") != null) {

            getDishesNameAndPositions(getOrder);

            deleteOrder(getOrder.jsonPath().getString("result.orders[0].order_id"));

            Assertions.assertNull(isClosedOrder(tableId));

        }

        return true;

    }

    @Step("Проверка что оплата прошла")
    public void closedOrderByApi(String tableId) {

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS).atMost(15, TimeUnit.MILLISECONDS)
                .timeout(Duration.ofSeconds(15)).untilAsserted(() ->
                        Assertions.assertTrue(deletePositionsAndOrder(tableId)));

    }

    public void deleteCertainPosition(String tableId, String orderId,ApiData.IikoData.Dish dish) {

        JsonPath jsonPath = getOrderInfo(tableId).jsonPath();
        List<Map<String, Object>> ordersElementAll = jsonPath.getList("result.ordersElementAll");

        String dishPositionId;

        for (Map<String, Object> element : ordersElementAll) {

            if (element.get("name").equals(dish.getName())) {

                dishPositionId = element.get("position_id").toString();

                Assertions.assertTrue(deletePosition(rqBodyDeletePosition(orderId,dishPositionId,dish.getId())));

                break;

            }

        }

    }

    public void allTypesModifiers(String guid) {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_TOMATO_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_ONION_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 2, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 2, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 2, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_TOMATO_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_ONION_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 3, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 3, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 3, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 2);
        createModificatorObject(dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, BURGER_ONION_PAID_NOT_NECESSARY_MODIFIER.getId(), 2);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 1, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_ONION_PAID_NOT_NECESSARY_MODIFIER.getId(), 2);
        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 2);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 1, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 2);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, BURGER.getId(), 1, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        createModificatorObject(dishesForFillingOrder, HOT_DOG_GORCHIZA_FREE_NECESSARY_MODIFIER.getId(), 1);
        createModificatorObject(dishesForFillingOrder, HOT_DOG_SOUS_FREE_NECESSARY_MODIFIER.getId(), 1);

        fillingOrder(rqBodyFillingOrderWithModifiers(guid, HOT_DOG.getId(), 1, dishesForFillingOrder));

    }




}
