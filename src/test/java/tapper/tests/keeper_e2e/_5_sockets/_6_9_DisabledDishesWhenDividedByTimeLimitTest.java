package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Stopwatch;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTestTwoBrowsers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.allDishesStatuses;


@Order(69)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Выбор позиций в раздельной оплате на 1-м устройстве, позиции в статусе “Оплачивается” на 2-м устройстве")
@DisplayName("Выбор позиций в раздельной оплате на 1-м устройстве, позиции в статусе “Оплачивается” на 2-м устройстве")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _6_9_DisabledDishesWhenDividedByTimeLimitTest extends BaseTestTwoBrowsers {

    static int amountDishesToBeChosen = 3;
    static int amountDishesForFillingOrder = 6;
    static String guid;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда, проверяем все суммы и условия")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);

        });

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);

        });

        using(firstBrowser, () -> {

            rootPage.isDishListNotEmptyAndVisible();
            rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);

        });

    }

    @Test
    @DisplayName("3. Проверяем что позиции закрыты для выбора и в статусе ожидается")
    public void savePaymentDataForAcquiring() {

        using(firstBrowser, Selenide::closeWindow);

        using(secondBrowser, () -> {

            Stopwatch stopwatch = Stopwatch.createStarted();

            for (SelenideElement element : allDishesStatuses) {

                System.out.println("Элементы еще в статусе 'Оплачивается'");
                element.shouldNotBe(text("Оплачивается"), Duration.ofSeconds(300));

            }

            stopwatch.stop();
            long millis = stopwatch.elapsed(TimeUnit.SECONDS);

            Assertions.assertTrue(millis >= 200,
                    "Время ожидания разделенных позиций меньше 4 мин (" + millis + ")");
            System.out.println("Время ожидания разделенных позиций соответствует 4 мин или больше (" + millis + " сек)");

            Allure.addAttachment
                    ("Время ожидания разделенных позиций в секундах", "text/plain", String.valueOf(millis));

        });

    }

    @Test
    @DisplayName("4. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);
        using(secondBrowser, Selenide::closeWindow);

    }

}
