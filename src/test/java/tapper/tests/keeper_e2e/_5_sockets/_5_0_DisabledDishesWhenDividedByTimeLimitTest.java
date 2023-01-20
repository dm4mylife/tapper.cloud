package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
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
import tests.BaseTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.text;
import static constants.Constant.TestData.*;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.allDishesStatuses;


@Order(50)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Выбор позиций в раздельной оплате на 1-м устройстве, позиции в статусе “Оплачивается” на 2-м устройстве")
@DisplayName("Выбор позиций в раздельной оплате на 1-м устройстве, позиции в статусе “Оплачивается” на 2-м устройстве")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_0_DisabledDishesWhenDividedByTimeLimitTest extends BaseTest {

    static String visit;
    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "3000"));

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда, проверяем все суммы и условия")
    public void chooseDishesAndCheckAfterDivided() {
        rootPageNestedTests.chooseDishesWithRandomAmount(3);
    }

    @Test
    @DisplayName("3. Очищаем все данные, делимся чеком")
    public void clearAllSiteData() {
        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);
    }

    @Test
    @DisplayName("4. Проверяем что позиции закрыты для выбора и в статусе ожидается")
    public void savePaymentDataForAcquiring() {

        Stopwatch stopwatch = Stopwatch.createStarted();

        for (SelenideElement element : allDishesStatuses) {
            System.out.println("Элементы еще в статусе 'Оплачивается'");
            element.shouldNotBe(text("Оплачивается"), Duration.ofSeconds(300));

        }

        stopwatch.stop();
        long millis = stopwatch.elapsed(TimeUnit.SECONDS);

        Assertions.assertTrue(millis >= 220, "Время ожидания разделенных позиций меньше 4 мин (" + millis + ")");
        System.out.println(millis + "Время ожидания разделенных позиций соответствует 4 мин или больше");

        Allure.addAttachment("Время ожидания разделенных позиций в секундах", "text/plain", String.valueOf(millis));

    }

    @Test
    @DisplayName("5. Закрываем заказ, очищаем кассу")
    public void closeOrder() {
        rootPageNestedTests.closeOrderByAPI(guid);
    }

}
