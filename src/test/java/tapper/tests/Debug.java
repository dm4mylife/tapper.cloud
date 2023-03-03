package tapper.tests;


import admin_personal_account.menu.Menu;
import admin_personal_account.operations_history.OperationsHistory;
import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import support_personal_account.lock.Lock;
import support_personal_account.logsAndPermissions.LogsAndPermissions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.Telegram;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import total_personal_account_actions.AuthorizationPage;

import java.text.ParseException;
import java.time.Duration;
import java.util.*;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Selenide.*;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.tipsWaiter;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Debug {

    static Response rsGetOrder;
    static Response rsFillingOrder;
    static String visit;
    static String guid;
    static String uni;
    static String orderType;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Lock lock = new Lock();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    Telegram telegram = new Telegram();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();


    //  <---------- Tests ---------->


    @Test
    public void gg() {

        apiRKeeper.getUniFirstValueFromOrderInfo(TABLE_AUTO_444_ID,AUTO_API_URI);

    }

    @Test
    void name() {

        Response rs = apiRKeeper.getOrderInfo("1000046", AUTO_API_URI);

        String totalPath = "";
        String session = "Session";
        String dish = "Dish";
        String price = "[\"@attributes\"].price";
        String name = "[\"@attributes\"].name";
        String quantity = "[\"@attributes\"].quantity";
        int totalDishIndex = 0;

        int sessionSize =  rootPageNestedTests.getKeySize(rs,session);

        HashMap<Integer,Map<String,Double>> orderData = new HashMap<>();

        for (int sessionIndex = 0; sessionIndex < sessionSize; sessionIndex++) {

            String sessionPath = rootPageNestedTests.getKeyPath(sessionSize,sessionIndex,session);
            // double discount = rootPageNestedTests.getDiscountFromResponse(rs);

            totalPath = sessionPath + "." + dish;

            int dishSize = rootPageNestedTests.getKeySize(rs,totalPath);

            for (int dishIndex = 0; dishIndex < dishSize; dishIndex++) {

                HashMap<String,Double> tempData = new HashMap<>();

                String dishPath = rootPageNestedTests.getKeyPath(dishSize,dishIndex,totalPath);

                double dishPrice = rs.jsonPath().getDouble(dishPath + price) / 100;
                String dishName = rs.jsonPath().getString(dishPath + name);

                int dishQuantity = rs.jsonPath().getInt(dishPath + quantity) / 1000;
                System.out.println(dishQuantity + " quantity");

                if (dishQuantity != 1) {

                    int dishQuantityIndex = 0;

                    for (; dishQuantityIndex < dishQuantity; dishQuantityIndex++) {

                        System.out.println(totalDishIndex+dishQuantityIndex + " counter");
                        tempData.put(dishName,dishPrice);
                        orderData.put(totalDishIndex + dishQuantityIndex,tempData);
                        System.out.println("Имя блюда : " + dishName + "\nЦена блюда : " + dishPrice + "\n");

                        System.out.println(orderData);

                    }

                    totalDishIndex += dishQuantityIndex;

                } else {

                    tempData.put(dishName,dishPrice);
                    orderData.put(totalDishIndex,tempData);
                    System.out.println("Имя блюда : " + dishName + "\nЦена блюда : " + dishPrice + "\n");

                }

                totalDishIndex++;

            }

        }

        System.out.println(orderData);

    }

    @Disabled
    @Test
    @DisplayName("info")
    public void getOrderInfo() {



        Response rs = apiRKeeper.getOrderInfo("1000044", TapperTable.AUTO_API_URI);

       // apiRKeeper.fillOrderWithAllModiDishes();

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName = null;

        int sessionDishSize = rs.jsonPath().getList("Session.Dish").size();
        System.out.println(sessionDishSize + " количество типов блюд\n");

        for (int currentDishIndex = 0; currentDishIndex <sessionDishSize; currentDishIndex++ ) {

            Map<String, Double> temporaryMap = new HashMap<>();

            double dishPrice = 0;
            int modificatorTypeSize = 0;

            if (rs.path("Session.Dish["+ currentDishIndex +"].Modi") != null) {

                if (rs.path("Session.Dish["+ currentDishIndex +"].Modi") instanceof LinkedHashMap) {

                    modificatorTypeSize = 1;

                } else {

                    modificatorTypeSize = rs.jsonPath().getList("Session.Dish["+ currentDishIndex +"].Modi").size();

                }

            }

            currentDishName = rs.jsonPath().getString("Session.Dish[" + currentDishIndex + "]['@attributes'].name");

            System.out.println(currentDishName + " имя текущего блюда");
            System.out.println(modificatorTypeSize + " количество типов модификатора у текущего типа блюд\n");

            double modificatorTotalPrice = 0;
            if (modificatorTypeSize == 1) {

                String modificatorName =
                        rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                "].Modi['@attributes'].name");
                System.out.println(modificatorName + " имя модификатора");

                String modificatorCurrentPriceFlag = rs.path(
                        "Session.Dish[" + currentDishIndex + "].Modi['@attributes'].price");

                double modificatorCurrentPrice = 0;

                if (modificatorCurrentPriceFlag != null) {

                    modificatorCurrentPrice = rs.jsonPath().getDouble
                            ("Session.Dish[" + currentDishIndex +
                                    "].Modi['@attributes'].price") / 100;

                }

                System.out.println(modificatorCurrentPrice + " цена текущего модификатора");

                int modificatorCurrentCount = rs.jsonPath().getInt
                        ("Session.Dish[" + currentDishIndex + "].Modi['@attributes'].count");
                System.out.println(modificatorCurrentCount + " текущее количество модификаторов");

                modificatorTotalPrice = modificatorCurrentPrice * modificatorCurrentCount;

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;
                System.out.println(currentDishPrice + " цена за само блюдо");

                dishPrice = currentDishPrice + modificatorTotalPrice ;

            } else {

                for (int currentModificatorTypeIndex = 0; currentModificatorTypeIndex < modificatorTypeSize; currentModificatorTypeIndex++) {

                    dishPrice = 0;

                    String modificatorName =
                            rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                    "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].name");
                    System.out.println(modificatorName + " имя модификатора");

                    String modificatorCurrentPriceFlag = rs.path(
                        "Session.Dish[" + currentDishIndex + "].Modi["
                                + currentModificatorTypeIndex + "]['@attributes'].price");

                    double modificatorCurrentPrice = 0;

                    if (modificatorCurrentPriceFlag != null) {

                        modificatorCurrentPrice = rs.jsonPath().getDouble
                            ("Session.Dish[" + currentDishIndex +
                                "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].price") / 100;

                    }

                    System.out.println(modificatorCurrentPrice + " цена текущего модификатора");

                    int modificatorCurrentCount = rs.jsonPath().getInt
                        ("Session.Dish[" + currentDishIndex + "].Modi["
                            + currentModificatorTypeIndex + "]['@attributes'].count");
                    System.out.println(modificatorCurrentCount + " текущее количество модификаторов");

                    modificatorTotalPrice += modificatorCurrentPrice * modificatorCurrentCount;
                    System.out.println(modificatorTotalPrice + " цена за количество и типы модификатора");

                }

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;
                System.out.println(currentDishPrice + " цена за само блюдо");

                dishPrice = currentDishPrice + modificatorTotalPrice ;

            }
            System.out.println(dishPrice + " общая цена за блюдо + сумма за его модики");

            int dishQuantity = rs.jsonPath().getInt("Session.Dish[" + currentDishIndex + "]['@attributes'].quantity") / 1000;

            for (int k = 0; k < dishQuantity; k++) {

                System.out.println("\nДобавлено в список под индексом " + totalDishIndex + "\n");
                temporaryMap.put(currentDishName, dishPrice);
                allDishesInfo.put(totalDishIndex, temporaryMap);

                totalDishIndex++;

            }

        }

        System.out.println("Итоговый список\n" + allDishesInfo);

        rootPage.forceWait(2000);



        rootPage.matchTapperOrderWithOrderInKeeper(allDishesInfo);



    }


    @Test
    @DisplayName("discount")
    public void deletePosition() {

        //  rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);

        Response rs = apiRKeeper.getOrderInfo("1000046", TapperTable.AUTO_API_URI);

        String session = "Session";

        Object sessionSizeFlag = rs.path(session);


        int sessionSize = 0;
        int sessionIndexCounter = 0;

        if (sessionSizeFlag instanceof LinkedHashMap) {

            sessionSize = 1;

        } else {

            sessionSize = rs.jsonPath().getList("Session").size();
            session = session + "[" + sessionIndexCounter + "]";

        }

        System.out.println(sessionSize + " количество сессий\n");

        for (; sessionIndexCounter < sessionSize; sessionIndexCounter++) {

            System.out.println(sessionIndexCounter + " текущая сессия");


            Object dishSizeSizeFlag = rs.path(session + ".Dish");

            String dish = ".Dish.";
            int dishSize = 0;
            int dishIndexCounter = 0;

            if (dishSizeSizeFlag instanceof LinkedHashMap) {

                dishSize = 1;

            } else {

                dishSize = rs.jsonPath().getList(session).size();
                dish = ".Dish[" + dishIndexCounter + "]";

            }

            System.out.println(dishSize + " количество блюд\n");

            for (; dishIndexCounter < dishSize; dishIndexCounter++) {

                String s = rs.jsonPath().getString(session + dish);

                System.out.println(dishIndexCounter + " текущее блюдо");

                System.out.println(s);

            }

        }


    }



    @Test
    @DisplayName("add discount")
    public void addDiscount() throws ParseException {


        AuthorizationPage authorizationPage = new AuthorizationPage();
        OperationsHistory operationsHistory = new OperationsHistory();
        RootPage rootPage = new RootPage();
        ApiRKeeper apiRKeeper = new ApiRKeeper();
        RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
        NestedTests nestedTests = new NestedTests();
        Menu menu = new Menu();

        rootPage.openPage("https://tapper.staging.zedform.ru");
        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        menu.goToMenuCategory();

        rootPage.forceWait(4000);

        SelenideElement element = $(".vAdminMenuAside__category:nth-child(1) .vAdminMenuCategoryItem__info");
        SelenideElement element2 = $(".vAdminMenuAside__category:nth-child(2) .vAdminMenuCategoryItem__info");
        SelenideElement elementDrag = $(".vAdminMenuAside__category:nth-child(1)");
        SelenideElement element2Drag = $(".vAdminMenuAside__category:nth-child(2)");


        Selenide.actions()
                .moveToElement(elementDrag)
                .clickAndHold()
                .pause(Duration.ofSeconds(2))
                .moveToElement(element2Drag)
                .perform();

        Selenide.actions()
                .moveToElement(element2Drag)
                .release()
                .build()
                .perform();



        rootPage.forceWait(3000);

    }

    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator1() {

        Response rs = apiRKeeper.getOrderInfo(TABLE_AUTO_111_ID, TapperTable.AUTO_API_URI);
        Object sessionSizeFlag = rs.path("Session");

        int sessionSize;
        int sessionIndexCounter = 0;
        int totalDiscountAmount = 0;

        if (sessionSizeFlag instanceof LinkedHashMap) {

            sessionSize = 1;

        } else {

            sessionSize = rs.jsonPath().getList("Session").size();

        }

        System.out.println(sessionSize + " количество сессий\n");

        for (; sessionIndexCounter < sessionSize; sessionIndexCounter++) {

            String session;

            if (sessionSize == 1) {
                session = "Session";
            } else {
                session = "Session" + "[" + sessionIndexCounter + "]";
            }

            System.out.println("\n" + sessionIndexCounter + " текущая сессия");

            Object discountOrderFlag = rs.path(session + ".Discount['@attributes'].amount");

            if (discountOrderFlag != null) {

                String discountOrderPath = ".Discount";
                Object discountFlag = rs.path(session + discountOrderPath);

                int discountSize;
                int discountIndexCounter = 0;

                if (discountFlag instanceof LinkedHashMap) {

                    discountSize = 1;

                } else {

                    discountSize = rs.jsonPath().getList(session + discountOrderPath).size();

                }

                for (; discountIndexCounter < discountSize; discountIndexCounter++) {

                    String discount;

                    if (discountSize == 1) {

                        discount = session + discountOrderPath;

                    } else {

                        discount = session + discountOrderPath + "[" + discountIndexCounter + "]";

                    }

                    double discountOrder = rs.jsonPath().getDouble(discount + "['@attributes'].amount") / 100;
                    System.out.println("\n" + discountOrder + " скидка по заказу");

                    totalDiscountAmount -= discountOrder;

                }

            }

            String dishPath = ".Dish";
            Object dishSizeSizeFlag = rs.path(session + dishPath);

            String dish = ".Dish";
            int dishSize;
            int dishIndexCounter = 0;

            if (dishSizeSizeFlag instanceof LinkedHashMap) {

                dishSize = 1;

            } else if (dishSizeSizeFlag == null) {

                dishSize = 0;

            } else {

                dishSize = rs.jsonPath().getList(session + dishPath).size();
                dish = dishPath + "[" + dishIndexCounter + "]";

            }

            System.out.println(dishSize + " количество блюд\n");

            for (; dishIndexCounter < dishSize; dishIndexCounter++) {

                Object discountDishFlag = rs.path(session + dish + ".Discount['@attributes'].amount");

                if (discountDishFlag != null) {

                    double discountDish = rs.jsonPath().getDouble(session + dish + ".Discount['@attributes'].amount") / 100;
                    System.out.println(discountDish + " скидка на блюдо");

                    totalDiscountAmount -= discountDish;


                }

            }

        }
        System.out.println("\n" + totalDiscountAmount + " общая скидка");

    }



}
