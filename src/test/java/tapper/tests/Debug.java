package tapper.tests;


import api.ApiRKeeper;
import com.codeborne.selenide.*;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.menu.Menu;
import admin_personal_account.operations_history.OperationsHistory;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.Telegram;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;

import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.TABLE_AUTO_1_ID;
import static com.codeborne.selenide.Selenide.*;
import static data.Constants.TestData.*;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.selectors.TapperTable.RootPage.DishList.divideCheckSlider;


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
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    Telegram telegram = new Telegram();

    //  <---------- Tests ---------->

    @Disabled
    @Test
    @DisplayName("kill")
    public void killTable() {

    DesiredCapabilities capabilities = new DesiredCapabilities();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--incognito");
    capabilities.setCapability(ChromeOptions.CAPABILITY,options);



    WebDriver firstBrowser = new ChromeDriver(options);
    firstBrowser.manage().window().setPosition(new Point(0,0));

    WebDriver secondBrowser = new ChromeDriver(options);
    secondBrowser.manage().window().setPosition(new Point(960,0));


     using(firstBrowser, () -> {

        open("https://stage-ssr.zedform.ru/testrkeeper/1000043");

     });

    using(secondBrowser, () -> {

        open("https://stage-ssr.zedform.ru/testrkeeper/1000043");

    });

    using(firstBrowser, () -> {

        sleep(5000);
        divideCheckSlider.click();
        rootPage.chooseCertainAmountDishes(1);

    });

    using(secondBrowser, () -> {

        sleep(5000);


    });



    firstBrowser.close();
    secondBrowser.close();



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

        rootPage.openUrlAndWaitAfter(TapperTable.STAGE_RKEEPER_TABLE_10);

        rootPage.matchTapperOrderWithOrderInKeeper(allDishesInfo);

       rootPageNestedTests.closeOrder();

    }

    @Disabled
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

        Response rs = apiRKeeper.getOrderInfo(TABLE_AUTO_1_ID, TapperTable.AUTO_API_URI);
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
